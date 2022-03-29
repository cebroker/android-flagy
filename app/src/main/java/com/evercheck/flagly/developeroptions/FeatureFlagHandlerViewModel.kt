package com.evercheck.flagly.developeroptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evercheck.flagly.developeroptions.adapter.FeatureFlagValueChangedListener
import com.evercheck.flagly.domain.model.FeatureFlag
import com.evercheck.flagly.domain.usecase.GetFeatureFlagUseCase
import com.evercheck.flagly.domain.usecase.SetFeatureFlagUseCase
import com.evercheck.flagly.utils.CoroutineContextProvider
import com.evercheck.flagly.utils.EMPTY
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeatureFlagHandlerViewModel @Inject constructor(
    private val getFeatureFlagUseCase: GetFeatureFlagUseCase,
    private val setFeatureFlagUseCase: SetFeatureFlagUseCase,
    private val coroutineContextProvider: CoroutineContextProvider,
) : ViewModel(), FeatureFlagValueChangedListener {

    private val _featureFlagState = MutableLiveData(FeatureFlagState())
    val featureFlagState: LiveData<FeatureFlagState> get() = _featureFlagState

    fun filterFeatureFlagsByName(query: String = EMPTY) {
        _featureFlagState.value = _featureFlagState.value?.copy(query = query)
        this.setupFeatureFlagValues()
    }

    private fun setupFeatureFlagValues() {
        viewModelScope.launch {
            withContext(coroutineContextProvider.io) {
                _featureFlagState.postValue(
                    _featureFlagState.value?.copy(
                        featureFlagValues = getFeatureFlagUseCase(
                            featureFlagState.value?.query ?: EMPTY
                        )
                    )
                )
            }
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
        setFeatureFlagUseCase(featureFlag, remoteValue, override)
        setupFeatureFlagValues()
    }
}