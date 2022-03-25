package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.domain.model.FeatureFlag
import io.mockk.mockk
import io.mockk.verify
import java.util.Random
import org.junit.Before
import org.junit.Test

class SetFeatureFlagUseCaseTest {

    private val localFeatureFlagHandler = mockk<DynamicFeatureFlagHandler>(relaxed = true)

    private lateinit var setFeatureFlagUseCase: SetFeatureFlagUseCase

    @Before
    fun setup() {
        setFeatureFlagUseCase = SetFeatureFlagUseCase(localFeatureFlagHandler)
    }

    private val featureFlag = object : FeatureFlag {
        override val name: String
            get() = "one"
    }

    @Test
    fun `given override true when Invoke is call `() {
        val numberRandom = Random().nextInt(2)
        val value = numberRandom % 2 == 0

        setFeatureFlagUseCase(featureFlag, value, remoteValue = true)

        if (value) {
            verify(exactly = 1) {
                localFeatureFlagHandler.setValue(featureFlag, value)
            }
        } else {
            verify(exactly = 1) {
                localFeatureFlagHandler.removeOverridenValue(featureFlag)
            }
        }
    }

    /*@Test
    fun `valid with the value from override is true or fale`() {
        val flag = mockk<FeatureFlag>()


        setFeatureFlagUseCase(featureFlag, value, remoteValue = true)

        if (value) {
            verify(exactly = 1) {
                localFeatureFlagHandler.setValue(featureFlag, value)
            }
        } else {
            verify(exactly = 1) {
                localFeatureFlagHandler.removeOverridenValue(featureFlag)
            }
        }
    }*/
}