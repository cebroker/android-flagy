package com.evercheck.flagly.di

import dagger.BindsInstance
import dagger.Component
import com.evercheck.flagly.developeroptions.FeatureFlagHandlerActivity
import com.evercheck.flagly.featureflag.DynamicFeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [FeatureHandlerModule::class])
interface FeatureHandlerComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            featureFlagProvider: FeatureFlagProvider,
            @BindsInstance
            remoteFeatureFlagHandler: FeatureFlagHandler,
            @BindsInstance
            localFeatureflagHandler: DynamicFeatureFlagHandler
        ): FeatureHandlerComponent
    }

    fun inject(featureFlagHandlerActivity: FeatureFlagHandlerActivity)
}