package com.evercheck.flagly.dataSource.remote

import com.evercheck.flagly.featureflag.FeatureFlag

interface  FeatureFlagHandler {

    fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean
}