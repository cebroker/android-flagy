package com.evercheck.flagly.developeroptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evercheck.flagly.developeroptions.adapter.FeatureFlagValueChangedListener
import com.evercheck.flagly.featureflag.DynamicFeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlag
import com.evercheck.flagly.featureflag.FeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagProvider
import com.evercheck.flagly.utils.EMPTY
import java.util.Locale
import javax.inject.Inject

class FeatureFlagHandlerViewModel @Inject constructor(
    private val featureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagHandler: FeatureFlagHandler,
    private val localFeatureflagHandler: DynamicFeatureFlagHandler,
) : ViewModel(), FeatureFlagValueChangedListener {

    private val _featureFlagValues = MutableLiveData<List<FeatureFlagValue>>()
    val featureFlagValues: LiveData<List<FeatureFlagValue>> get() = _featureFlagValues

    private lateinit var query: String

    fun filterFeatureFlagsByName(query: String = EMPTY) {
        this.query = query
        this.setupFeatureFlagValues()
    }

    private fun setupFeatureFlagValues() {
        _featureFlagValues.postValue(featureFlagProvider.provideAppSupportedFeatureflags()
            .filter { it.name.toLowerCase(Locale.ROOT).contains(query) }
            .map { featureFlag ->
                getFeatureFlagValue(featureFlag)
            })
    }

    override fun onFeatureFlagValueChanged(featureFlag: FeatureFlag, value: Boolean) {
        localFeatureflagHandler.setValue(featureFlag, value)
    }

    override fun onOverrideValueChange(
        featureFlag: FeatureFlag,
        override: Boolean,
        remoteValue: Boolean
    ) {
        if (override) {
            localFeatureflagHandler.setValue(featureFlag, remoteValue)
        } else {
            localFeatureflagHandler.removeOverridenValue(featureFlag)
        }
        setupFeatureFlagValues()
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