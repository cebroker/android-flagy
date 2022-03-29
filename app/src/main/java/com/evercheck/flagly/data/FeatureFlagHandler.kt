package com.evercheck.flagly.data

import com.evercheck.flagly.domain.model.FeatureFlag

interface  FeatureFlagHandler {

    fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean
}