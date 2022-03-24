package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagProvider
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.domain.model.FeatureFlag
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Locale
import kotlinx.coroutines.runBlocking
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

    @Test
    fun `get movie from remote when is not found in local storage`() {
        val featureFlagOne = object : FeatureFlag {
            override val name: String
                get() = "One"
        }
        val featureFlagTwo = object : FeatureFlag {
            override val name: String
                get() = "Two"
        }
        val featureFlagThree = object : FeatureFlag {
            override val name: String
                get() = "Three"
        }


        val featureFlagMock = listOf<FeatureFlag>(featureFlagOne, featureFlagTwo, featureFlagThree)
        val expected = listOf<FeatureFlagValue>(FeatureFlagValue(featureFlagTwo))

        every {
            featureFlagProvider.provideAppSupportedFeatureflags()
        } answers {
            featureFlagMock
        }

        val response = getFeatureFlagUseCase(QUERY_DEFAULT_TEST)

        verify(exactly = 1) {
            featureFlagProvider.provideAppSupportedFeatureflags()
        }

        Assert.assertEquals(expected, response)
    }
}

private const val QUERY_DEFAULT_TEST = "TWO"