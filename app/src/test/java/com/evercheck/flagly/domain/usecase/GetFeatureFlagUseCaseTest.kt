package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagProvider
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.domain.model.FeatureFlag
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetFeatureFlagUseCaseTest {

    private val featureFlagProvider = mockk<FeatureFlagProvider>(relaxed = true)
    private val remoteFeatureFlagHandler = mockk<FeatureFlagHandler>(relaxed = true)
    private val localFeatureFlagHandler = mockk<DynamicFeatureFlagHandler>(relaxed = true)
    private lateinit var getFeatureFlagUseCase: GetFeatureFlagUseCase
    private val flags = listOf<FeatureFlag>(mockk(), mockk(), mockk(), mockk(), mockk())

    init {
        flags.forEachIndexed { index, it ->
            every {
                it.name
            } answers {
                "Flag$index"
            }
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

    @Test
    fun `given a list of feature flags and a random feature when searching for a feature then you get a list`() {
        val values = flags.map { FeatureFlagValue(it) }.toMutableList()
        values[0] = FeatureFlagValue(values[0].featureFlag, true)

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            flags
        }
        every {
            localFeatureFlagHandler.isValueOverriden(flags[0])
        } answers {
            true
        }

        val response = getFeatureFlagUseCase(QUERY_SEARCH_TEST)

        verify(exactly = 1) {
            featureFlagProvider.provideAppSupportedFeatureflags()

            localFeatureFlagHandler.isValueOverriden(flags[0])
            remoteFeatureFlagHandler.isFeatureEnabled(flags[0])
        }
        Assert.assertEquals(values, response)
    }

    @Test
    fun `given a list of feature flags values the override and current value are true when searching for a feature then used local isFeatureEnabled`() {
        val values = flags.map { FeatureFlagValue(it) }.toMutableList()
        values[0] = FeatureFlagValue(values[0].featureFlag, isOverride = true, currentValue = true)

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            flags
        }
        every {
            localFeatureFlagHandler.isFeatureEnabled(flags[0])
        } answers {
            true
        }
        every {
            localFeatureFlagHandler.isValueOverriden(flags[0])
        } answers {
            true
        }

        val response = getFeatureFlagUseCase(QUERY_SEARCH_TEST)

        verify(exactly = 1) {
            featureFlagProvider.provideAppSupportedFeatureflags()
            localFeatureFlagHandler.isValueOverriden(flags[0])
            remoteFeatureFlagHandler.isFeatureEnabled(flags[0])
        }

        verify(exactly = 2) {
            localFeatureFlagHandler.isFeatureEnabled(flags[0])
        }
        Assert.assertEquals(values, response)
    }
}

private const val QUERY_SEARCH_TEST = "fl"