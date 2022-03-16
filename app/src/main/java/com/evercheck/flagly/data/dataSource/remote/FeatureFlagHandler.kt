package com.evercheck.flagly.data.dataSource.remote

import com.evercheck.flagly.data.model.FeatureFlag

interface  FeatureFlagHandler {

    fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean
}