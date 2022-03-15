package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.featureflag.DynamicFeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlag
import com.evercheck.flagly.featureflag.FeatureFlagHandler
import com.evercheck.flagly.featureflag.FeatureFlagProvider
import com.evercheck.flagly.utils.CoroutineContextProvider
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

class FeatureFlagHandlerPresenter @Inject constructor(
    private val featureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagHandler: FeatureFlagHandler,
    private val localFeatureflagHandler: DynamicFeatureFlagHandler,
    private val coroutineContextProvider: CoroutineContextProvider
) : FeatureFlagActivityContract.Presenter, CoroutineScope {

    private val job: Job = SupervisorJob()
    private lateinit var query: String

    override var view: FeatureFlagActivityContract.View? = null

    override val coroutineContext: CoroutineContext
        get() = coroutineContextProvider.mainDispatcher + job

    override fun bind(view: FeatureFlagActivityContract.View) {
        this.view = view
    }

    override fun onViewReady() {
        view?.setup()
    }

    override fun filterFeatureFlagsByName(query: String) {
        this.query = query
        this.setupFeatureFlagValues()
    }

    private fun setupFeatureFlagValues() {
        val values = featureFlagProvider.provideAppSupportedFeatureflags()
            .filter { it.name.toLowerCase(Locale.ROOT).contains(query) }
            .map { featureFlag ->
                getFeatureFlagValue(featureFlag)
            }

        view?.showReatureFlagValues(values)
    }

    override fun unBind() {
        view = null
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