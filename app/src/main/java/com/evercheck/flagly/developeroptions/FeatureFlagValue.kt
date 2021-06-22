package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.featureflag.FeatureFlag

data class FeatureFlagValue(
    val featureFlag: FeatureFlag,
    val isOverride: Boolean,
    val currentValue: Boolean,
    val remoteValue: Boolean
)
