package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagProvider
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.domain.model.FeatureFlag
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Random

class GetFeatureFlagUseCaseTest {

    private val featureFlagProvider = mockk<FeatureFlagProvider>(relaxed = true)
    private val remoteFeatureFlagHandler = mockk<FeatureFlagHandler>(relaxed = true)
    //private val localFeatureFlagHandler = mockk<DynamicFeatureFlagHandler>(relaxed = true)

    private lateinit var getFeatureFlagUseCase: GetFeatureFlagUseCase

    private val localFeatureFlagHandler = object : DynamicFeatureFlagHandler {
        private val featureflagMap = HashMap<String, Boolean>()

        override fun setValue(featureFlag: FeatureFlag, value: Boolean) {
            featureflagMap[featureFlag.name] = value
        }

        override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean {
            return featureflagMap[featureFlag.name] ?: false
        }

        override fun isValueOverriden(featureFlag: FeatureFlag): Boolean =
            featureflagMap.containsKey(featureFlag.name)

        override fun removeOverridenValue(featureFlag: FeatureFlag) {
            featureflagMap.remove(featureFlag.name)
        }
    }

    @Before
    fun setup() {
        getFeatureFlagUseCase = GetFeatureFlagUseCase(
            featureFlagProvider,
            remoteFeatureFlagHandler,
            localFeatureFlagHandler
        )
    }

    private val characters = ('a'..'z') + ('A'..'Z')
    private fun generateFeatureFlag(): List<FeatureFlag> = (0..Random().nextInt(6)).map {
        characters.shuffled()[it].toString()
    }.toSet()
        .map {
            object : FeatureFlag {
                override val name: String
                    get() = it
            }
        }

    @Test
    fun `given a list of feature flags and a random feature when searching for a feature then you get a list`() {
        val featureFlags = generateFeatureFlag()
        val numberRandom = Random().nextInt(featureFlags.size)
        val featureFlagValueRandom = FeatureFlagValue(featureFlags[numberRandom])
        val featureFlagsValuesFilter = featureFlags.filter {
            it.name.contains(featureFlagValueRandom.featureFlag.name, true)
        }.map { FeatureFlagValue(it) }

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            featureFlags
        }

        val response = getFeatureFlagUseCase(featureFlagValueRandom.featureFlag.name)

        verify(exactly = 1) {
            featureFlagProvider.provideAppSupportedFeatureflags()

            localFeatureFlagHandler.isValueOverriden(featureFlagValueRandom.featureFlag)
            remoteFeatureFlagHandler.isFeatureEnabled(featureFlagValueRandom.featureFlag)
        }
        Assert.assertEquals(featureFlagsValuesFilter, response)
    }

    @Test
    fun `given a list of feature flags values the override and current value are true when searching for a feature then used local isFeatureEnabled`() {
        val featureFlags = generateFeatureFlag()
        val numberRandom = Random().nextInt(featureFlags.size)
        val featureFlagValueRandom = FeatureFlagValue(featureFlags[numberRandom])
        val featureFlagsValuesFilter = featureFlags.filter {
            it.name.contains(featureFlagValueRandom.featureFlag.name, true)
        }.map { FeatureFlagValue(it, isOverride = true, currentValue = true) }

        localFeatureFlagHandler.setValue(featureFlagValueRandom.featureFlag, true)

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            featureFlags
        }

        val response = getFeatureFlagUseCase(featureFlagValueRandom.featureFlag.name)

        verify(exactly = 1) {
            featureFlagProvider.provideAppSupportedFeatureflags()

            localFeatureFlagHandler.isValueOverriden(featureFlagValueRandom.featureFlag)
            remoteFeatureFlagHandler.isFeatureEnabled(featureFlagValueRandom.featureFlag)

            localFeatureFlagHandler.isFeatureEnabled(featureFlagValueRandom.featureFlag)
        }
        Assert.assertEquals(featureFlagsValuesFilter, response)
    }
}