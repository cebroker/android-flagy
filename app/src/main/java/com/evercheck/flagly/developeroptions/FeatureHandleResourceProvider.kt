package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.featureflag.DynamicFeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagProvider

interface FeatureHandleResourceProvider {

    fun getFeatureFlagProvider(): FeatureFlagProvider
    fun getRemoteFeatureFlagHandler(): FeatureFlagHandler
    fun getLocalFeatureflagHandler(): DynamicFeatureFlagHandler
}