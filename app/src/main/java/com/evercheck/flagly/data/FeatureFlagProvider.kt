package com.evercheck.flagly.data

import com.evercheck.flagly.domain.model.FeatureFlag

interface FeatureFlagProvider {

    fun provideAppSupportedFeatureflags(): Collection<FeatureFlag>
}
