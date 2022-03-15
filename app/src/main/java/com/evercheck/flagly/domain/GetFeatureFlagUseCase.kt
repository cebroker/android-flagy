package com.evercheck.flagly.domain

import com.evercheck.flagly.dataSource.FeatureFlagProvider
import com.evercheck.flagly.dataSource.local.DynamicFeatureFlagHandler
import com.evercheck.flagly.dataSource.remote.FeatureFlagHandler
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.featureflag.FeatureFlag
import com.evercheck.flagly.utils.EMPTY
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class GetFeatureFlagUseCase @Inject constructor(
    private val featureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagHandler: FeatureFlagHandler,
    private val localFeatureflagHandler: DynamicFeatureFlagHandler
) {

    private val job: Job = SupervisorJob()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    suspend operator fun invoke(query: String = EMPTY): List<FeatureFlagValue> =
        withContext(coroutineContext) {
            featureFlagProvider.provideAppSupportedFeatureflags()
                .filter { it.name.lowercase(Locale.ROOT).contains(query) }
                .map { featureFlag ->
                    getFeatureFlagValue(featureFlag)
                }
        }

    private fun getFeatureFlagValue(featureFlag: FeatureFlag): FeatureFlagValue {
        val isOverride = localFeatureflagHandler.isValueOverriden(featureFlag)
        return FeatureFlagValue(
            featureFlag,
            isOverride,
            getLocalValue(featureFlag, getLocalValue(featureFlag, isOverride)),
            remoteFeatureFlagHandler.isFeatureEnabled(featureFlag)
        )
    }

    private fun getLocalValue(featureFlag: FeatureFlag, isOverride: Boolean): Boolean =
        if (isOverride) {
            localFeatureflagHandler.isFeatureEnabled(featureFlag)
        } else {
            false
        }
}