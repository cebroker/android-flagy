package com.evercheck.flagly.developeroptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evercheck.flagly.data.model.FeatureFlag
import com.evercheck.flagly.developeroptions.adapter.FeatureFlagValueChangedListener
import com.evercheck.flagly.domain.data.State
import com.evercheck.flagly.domain.useCase.GetFeatureFlagUseCase
import com.evercheck.flagly.domain.useCase.SetFeatureFlagUseCase
import com.evercheck.flagly.utils.EMPTY
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeatureFlagHandlerViewModel @Inject constructor(
    private val getFeatureFlagUseCase: GetFeatureFlagUseCase,
    private val setFeatureFlagUseCase: SetFeatureFlagUseCase
) : ViewModel(), FeatureFlagValueChangedListener {

    private val _state = MutableLiveData<State>(State())
    val state: LiveData<State> get() = _state

    fun filterFeatureFlagsByName(query: String = EMPTY) {
        _state.value = _state.value?.copy(query = query)
        this.setupFeatureFlagValues()
    }

    private fun setupFeatureFlagValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO + SupervisorJob()) {
                _state.postValue(
                    _state.value?.copy(
                        featureFlagValues = getFeatureFlagUseCase(
                            state.value?.query ?: EMPTY
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
        setFeatureFlagUseCase(featureFlag, override, remoteValue)
        setupFeatureFlagValues()
    }
}