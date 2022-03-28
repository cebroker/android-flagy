package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.domain.model.FeatureFlag
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.Random

class SetFeatureFlagUseCaseTest {

    private val localFeatureFlagHandler = mockk<DynamicFeatureFlagHandler>(relaxed = true)

    private lateinit var setFeatureFlagUseCase: SetFeatureFlagUseCase

    private val featureFlag = object : FeatureFlag {
        override val name: String
            get() = "one"
    }

    @Before
    fun setup() {
        setFeatureFlagUseCase = SetFeatureFlagUseCase(localFeatureFlagHandler)
    }

    @Test
    fun `given override true when invoking the call then if the override is true you should execute the setValue method else remove override value `() {
        val override = true
        setFeatureFlagUseCase(featureFlag, true, override)

        if (override) {
            verify(exactly = 1) {
                localFeatureFlagHandler.setValue(featureFlag, override)
            }
        } else {
            verify(exactly = 1) {
                localFeatureFlagHandler.removeOverridenValue(featureFlag)
            }
        }
    }

    @Test
    fun `given override false when invoking the call then if the override is true you should execute the setValue method else remove override value `() {
        val override = false

        setFeatureFlagUseCase(featureFlag, true, override)

        if (override) {
            verify(exactly = 1) {
                localFeatureFlagHandler.setValue(featureFlag, override)
            }
        } else {
            verify(exactly = 1) {
                localFeatureFlagHandler.removeOverridenValue(featureFlag)
            }
        }
    }
}