package com.evercheck.flagly.data.dataSource.local

import com.evercheck.flagly.data.dataSource.remote.FeatureFlagHandler
import com.evercheck.flagly.exceptions.FeatureFlagNotPresentInHandlerException
import com.evercheck.flagly.data.model.FeatureFlag

interface DynamicFeatureFlagHandler : FeatureFlagHandler {

    @Throws(FeatureFlagNotPresentInHandlerException::class)
    override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean

    fun setValue(featureFlag: FeatureFlag, value: Boolean)

    fun isValueOverriden(featureFlag: FeatureFlag): Boolean

    fun removeOverridenValue(featureFlag: FeatureFlag)
}