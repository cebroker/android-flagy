package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.domain.model.FeatureFlag

data class FeatureFlagValue(
    val featureFlag: FeatureFlag,
    val isOverride: Boolean = false,
    val currentValue: Boolean = false,
    val remoteValue: Boolean = false
)
