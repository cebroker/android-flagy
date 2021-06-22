package com.evercheck.flagly.featureflag

interface FeatureFlagProvider {

    fun provideAppSupportedFeatureflags(): Collection<FeatureFlag>
}
