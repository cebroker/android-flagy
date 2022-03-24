package com.evercheck.flagly.data

import com.evercheck.flagly.domain.exceptions.FeatureFlagNotPresentInHandlerException
import com.evercheck.flagly.domain.model.FeatureFlag

interface DynamicFeatureFlagHandler : FeatureFlagHandler {

    @Throws(FeatureFlagNotPresentInHandlerException::class)
    override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean

    fun setValue(featureFlag: FeatureFlag, value: Boolean)

    fun isValueOverriden(featureFlag: FeatureFlag): Boolean

    fun removeOverridenValue(featureFlag: FeatureFlag)
}