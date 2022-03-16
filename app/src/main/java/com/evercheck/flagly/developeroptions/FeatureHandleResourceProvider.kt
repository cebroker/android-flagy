package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.data.dataSource.local.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.dataSource.remote.FeatureFlagHandler
import com.evercheck.flagly.data.dataSource.FeatureFlagProvider

interface FeatureHandleResourceProvider {

    fun getFeatureFlagProvider(): FeatureFlagProvider
    fun getRemoteFeatureFlagHandler(): FeatureFlagHandler
    fun getLocalFeatureflagHandler(): DynamicFeatureFlagHandler
}