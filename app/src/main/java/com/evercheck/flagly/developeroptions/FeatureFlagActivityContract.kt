package com.evercheck.flagly.developeroptions

import com.evercheck.flagly.developeroptions.adapter.FeatureFlagValueChangedListener
import com.evercheck.flagly.utils.EMPTY_STRING

interface FeatureFlagActivityContract {

    interface View {

        fun setup()

        fun showReatureFlagValues(featureFlagValues: List<FeatureFlagValue>)
    }

    interface Presenter : FeatureFlagValueChangedListener {

        var view: View?

        fun bind(view: View)

        fun filterFeautreFlagsByName(search: String = EMPTY_STRING)

        fun onViewReady()

        fun unBind()
    }
}
