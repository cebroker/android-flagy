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

    @Before
    fun setup() {
        setFeatureFlagUseCase = SetFeatureFlagUseCase(localFeatureFlagHandler)
    }

    private val featureFlag = object : FeatureFlag {
        override val name: String
            get() = "one"
    }

    @Test
    fun `given override true or false when invoking the call then if the override is true you should execute the setValue method else removeOverridenValue `() {
        val numberRandom = Random().nextInt(2)
        val override = numberRandom % 2 == 0

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