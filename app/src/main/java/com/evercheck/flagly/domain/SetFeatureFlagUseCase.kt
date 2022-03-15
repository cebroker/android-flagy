package com.evercheck.flagly.domain

import com.evercheck.flagly.dataSource.local.DynamicFeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlag
import javax.inject.Inject

class SetFeatureFlagUseCase @Inject constructor(
    private val localFeatureflagHandler: DynamicFeatureFlagHandler
) {

    operator fun invoke(featureFlag: FeatureFlag, value: Boolean) {
        localFeatureflagHandler.setValue(featureFlag, value)
    }

    operator fun invoke(featureFlag: FeatureFlag, override: Boolean, remoteValue: Boolean) {
        if (override) {
            localFeatureflagHandler.setValue(featureFlag, remoteValue)
        } else {
            localFeatureflagHandler.removeOverridenValue(featureFlag)
        }
    }
}