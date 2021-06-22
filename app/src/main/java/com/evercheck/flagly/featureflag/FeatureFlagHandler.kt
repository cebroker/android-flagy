package com.evercheck.flagly.featureflag

interface  FeatureFlagHandler {

    fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean
}