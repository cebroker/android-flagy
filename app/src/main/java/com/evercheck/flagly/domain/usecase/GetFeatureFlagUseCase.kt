package com.evercheck.flagly.domain.usecase

import com.evercheck.flagly.data.FeatureFlagProvider
import com.evercheck.flagly.data.DynamicFeatureFlagHandler
import com.evercheck.flagly.data.FeatureFlagHandler
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.domain.model.FeatureFlag
import com.evercheck.flagly.utils.EMPTY
import java.util.Locale
import javax.inject.Inject

class GetFeatureFlagUseCase @Inject constructor(
    private val featureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagHandler: FeatureFlagHandler,
    private val localFeatureFlagHandler: DynamicFeatureFlagHandler
) {
    operator fun invoke(query: String = EMPTY): List<FeatureFlagValue> =
        featureFlagProvider.provideAppSupportedFeatureflags()
            .filter { it.name.contains(query, true) }
            .map { featureFlag ->
                getFeatureFlagValue(featureFlag)
            }

    private fun getFeatureFlagValue(featureFlag: FeatureFlag): FeatureFlagValue {
        val isOverride = localFeatureFlagHandler.isValueOverriden(featureFlag)
        return FeatureFlagValue(
            featureFlag,
            isOverride,
            getLocalValue(featureFlag, getLocalValue(featureFlag, isOverride)),
            remoteFeatureFlagHandler.isFeatureEnabled(featureFlag)
        )
    }

    private fun getLocalValue(featureFlag: FeatureFlag, isOverride: Boolean): Boolean =
        if (isOverride) {
            localFeatureFlagHandler.isFeatureEnabled(featureFlag)
        } else {
            false
        }
}