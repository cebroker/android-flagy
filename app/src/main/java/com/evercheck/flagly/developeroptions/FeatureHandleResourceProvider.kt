package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagProvider

interface FeatureHandleResourceProvider {

    fun getFeatureFlagProvider(): FeatureFlagProvider
    fun getRemoteFeatureFlagHandler(): FeatureFlagHandler
    fun getLocalFeatureflagHandler(): DynamicFeatureFlagHandler
}