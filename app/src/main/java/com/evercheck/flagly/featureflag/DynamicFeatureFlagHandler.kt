package com.evercheck.flagly.featureflag

import com.evercheck.flagly.exceptions.FeatureFlagNotPresentInHandlerException

interface DynamicFeatureFlagHandler : FeatureFlagHandler {

    @Throws(FeatureFlagNotPresentInHandlerException::class)
    override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean

    fun setValue(featureFlag: FeatureFlag, value: Boolean)

    fun isValueOverriden(featureFlag: FeatureFlag): Boolean

    fun removeOverridenValue(featureFlag: FeatureFlag)
}