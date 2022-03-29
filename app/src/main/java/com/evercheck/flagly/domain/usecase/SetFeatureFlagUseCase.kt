package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.domain.model.FeatureFlag
import javax.inject.Inject

class SetFeatureFlagUseCase @Inject constructor(
    private val localFeatureflagHandler: DynamicFeatureFlagHandler
) {

    operator fun invoke(featureFlag: FeatureFlag, remoteValue: Boolean, override: Boolean = true) {
        if (override) {
            localFeatureflagHandler.setValue(featureFlag, remoteValue)
        } else {
            localFeatureflagHandler.removeOverridenValue(featureFlag)
        }
    }
}