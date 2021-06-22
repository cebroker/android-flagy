package com.evercheck.flagly.di

import com.evercheck.flagly.developeroptions.FeatureFlagActivityContract
import com.evercheck.flagly.developeroptions.FeatureFlagHandlerPresenter
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import com.evercheck.flagly.utils.CoroutineContextProvider
import javax.inject.Singleton

@Module
object FeatureHandlerModule {

    @Singleton
    @Provides
    fun provideCoroutineContextProvider() =
        CoroutineContextProvider(Dispatchers.Main, Dispatchers.IO)

    @Singleton
    @Provides
    fun providePresenter(featureFlagHandlerPresenter: FeatureFlagHandlerPresenter): FeatureFlagActivityContract.Presenter =
        featureFlagHandlerPresenter
}