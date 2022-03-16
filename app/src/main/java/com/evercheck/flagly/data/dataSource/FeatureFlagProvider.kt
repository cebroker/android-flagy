package com.evercheck.flagly.data.dataSource

import com.evercheck.flagly.data.model.FeatureFlag

interface FeatureFlagProvider {

    fun provideAppSupportedFeatureflags(): Collection<FeatureFlag>
}
