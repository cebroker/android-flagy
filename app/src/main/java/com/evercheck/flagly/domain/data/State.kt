package com.evercheck.flagly.domain.data

import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.utils.EMPTY

data class State(
    val query: String = EMPTY,
    val featureFlagValues: List<FeatureFlagValue> = listOf()
)
