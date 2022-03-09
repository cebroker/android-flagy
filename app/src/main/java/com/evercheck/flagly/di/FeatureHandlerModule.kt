package com.evercheck.flagly.di

import com.evercheck.flagly.utils.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
object FeatureHandlerModule {

    @Singleton
    @Provides
    fun provideCoroutineContextProvider() =
        CoroutineContextProvider(Dispatchers.Main, Dispatchers.IO)
}