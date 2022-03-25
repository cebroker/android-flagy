package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagProvider
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.domain.model.FeatureFlag
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Random
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetFeatureFlagUseCaseTest {

    private val featureFlagProvider = mockk<FeatureFlagProvider>(relaxed = true)
    private val remoteFeatureFlagHandler = mockk<FeatureFlagHandler>(relaxed = true)
    private val localFeatureFlagHandler = mockk<DynamicFeatureFlagHandler>(relaxed = true)

    private lateinit var getFeatureFlagUseCase: GetFeatureFlagUseCase

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
        characters.shuffled().get(it).toString()
    }.toSet()
        .map {
            object : FeatureFlag {
                override val name: String
                    get() = it
            }
        }

    @Test
    fun `get a specific feature flag`() {
        val featureFlagsValues = generateFeatureFlag()
        val numberRandom = Random().nextInt(featureFlagsValues.size)
        val featureFlagRandom = FeatureFlagValue(featureFlagsValues[numberRandom])

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            featureFlagsValues
        }

        val response = getFeatureFlagUseCase(featureFlagRandom.featureFlag.name)

        verify(exactly = 1) {
            featureFlagProvider.provideAppSupportedFeatureflags()

            localFeatureFlagHandler.isValueOverriden(featureFlagRandom.featureFlag)
            remoteFeatureFlagHandler.isFeatureEnabled(featureFlagRandom.featureFlag)
        }
        Assert.assertEquals(featureFlagRandom, response[0])
    }

    @Test
    fun `validate when a feature is overwritten`() {
        val featureFlagsValues = generateFeatureFlag()
        val numberRandom = Random().nextInt(featureFlagsValues.size)
        val featureFlagRandom = FeatureFlagValue(featureFlagsValues[numberRandom])

        localFeatureFlagHandler.setValue(featureFlagRandom.featureFlag, true)

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            featureFlagsValues
        }

        val response = getFeatureFlagUseCase(featureFlagRandom.featureFlag.name)

        verify(exactly = 1) {
            localFeatureFlagHandler.isFeatureEnabled(featureFlagRandom.featureFlag)
        }
        Assert.assertEquals(featureFlagRandom, response[0])
    }
}