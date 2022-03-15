package com.evercheck.flagly.developeroptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evercheck.flagly.developeroptions.adapter.FeatureFlagValueChangedListener
import com.evercheck.flagly.domain.GetFeatureFlagUseCase
import com.evercheck.flagly.domain.SetFeatureFlagUseCase
import com.evercheck.flagly.featureflag.FeatureFlag
import com.evercheck.flagly.utils.EMPTY
import javax.inject.Inject
import kotlinx.coroutines.launch

class FeatureFlagHandlerViewModel @Inject constructor(
    private val getFeatureFlagUseCase: GetFeatureFlagUseCase,
    private val setFeatureFlagUseCase: SetFeatureFlagUseCase
) : ViewModel(), FeatureFlagValueChangedListener {

    private val _featureFlagValues = MutableLiveData<List<FeatureFlagValue>>()
    val featureFlagValues: LiveData<List<FeatureFlagValue>> get() = _featureFlagValues

    private lateinit var query: String

    fun filterFeatureFlagsByName(query: String = EMPTY) {
        this.query = query
        this.setupFeatureFlagValues()
    }

    private fun setupFeatureFlagValues() {
        viewModelScope.launch {
            _featureFlagValues.postValue(getFeatureFlagUseCase(query))
        }
    }

    override fun onFeatureFlagValueChanged(featureFlag: FeatureFlag, value: Boolean) {
        setFeatureFlagUseCase(featureFlag, value)
    }

    override fun onOverrideValueChange(
        featureFlag: FeatureFlag,
        override: Boolean,
        remoteValue: Boolean
    ) {
        setFeatureFlagUseCase(featureFlag, override, remoteValue)
        setupFeatureFlagValues()
    }
}