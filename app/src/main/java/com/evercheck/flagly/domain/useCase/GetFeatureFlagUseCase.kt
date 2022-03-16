package com.evercheck.flagly.domain.useCase

import com.evercheck.flagly.data.dataSource.FeatureFlagProvider
import com.evercheck.flagly.data.dataSource.local.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.dataSource.remote.FeatureFlagHandler
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.data.model.FeatureFlag
import com.evercheck.flagly.utils.EMPTY
import java.util.Locale
import javax.inject.Inject

class GetFeatureFlagUseCase @Inject constructor(
    private val featureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagHandler: FeatureFlagHandler,
    private val localFeatureflagHandler: DynamicFeatureFlagHandler
) {
    operator fun invoke(query: String = EMPTY): List<FeatureFlagValue> =
        featureFlagProvider.provideAppSupportedFeatureflags()
            .filter { it.name.lowercase(Locale.ROOT).contains(query) }
            .map { featureFlag ->
                getFeatureFlagValue(featureFlag)
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