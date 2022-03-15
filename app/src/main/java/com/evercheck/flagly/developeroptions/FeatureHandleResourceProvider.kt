package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.dataSource.local.DynamicFeatureFlagHandler
import com.evercheck.flagly.dataSource.remote.FeatureFlagHandler
import com.evercheck.flagly.dataSource.FeatureFlagProvider

interface FeatureHandleResourceProvider {

    fun getFeatureFlagProvider(): FeatureFlagProvider
    fun getRemoteFeatureFlagHandler(): FeatureFlagHandler
    fun getLocalFeatureflagHandler(): DynamicFeatureFlagHandler
}