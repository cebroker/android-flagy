package com.evercheck.flagly.dataSource.local

import com.evercheck.flagly.dataSource.remote.FeatureFlagHandler
import com.evercheck.flagly.exceptions.FeatureFlagNotPresentInHandlerException
import com.evercheck.flagly.featureflag.FeatureFlag

interface DynamicFeatureFlagHandler : FeatureFlagHandler {

    @Throws(FeatureFlagNotPresentInHandlerException::class)
    override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean

    fun setValue(featureFlag: FeatureFlag, value: Boolean)

    fun isValueOverriden(featureFlag: FeatureFlag): Boolean

    fun removeOverridenValue(featureFlag: FeatureFlag)
}