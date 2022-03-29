package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.utils.EMPTY

data class FeatureFlagState(
    val query: String = EMPTY,
    val featureFlagValues: List<FeatureFlagValue> = listOf()
)
