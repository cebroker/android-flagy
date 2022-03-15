package com.evercheck.flagly.dataSource

import com.evercheck.flagly.featureflag.FeatureFlag

interface FeatureFlagProvider {

    fun provideAppSupportedFeatureflags(): Collection<FeatureFlag>
}
