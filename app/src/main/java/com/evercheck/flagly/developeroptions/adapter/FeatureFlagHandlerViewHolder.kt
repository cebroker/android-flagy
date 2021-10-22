package com.evercheck.flagly.developeroptions.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.evercheck.flagly.databinding.ItemFeatureFlagBinding
import kotlinx.android.extensions.LayoutContainer
import com.evercheck.flagly.developeroptions.FeatureFlagValue

class FeatureFlagHandlerViewHolder(private val binding: ItemFeatureFlagBinding) :
    RecyclerView.ViewHolder(binding.root), LayoutContainer {

    override val containerView: View = binding.root

    fun bind(
        featureFlagValue: FeatureFlagValue,
        featureFlagValueChangedListenerListener: FeatureFlagValueChangedListener
    ) {
        with(binding) {
            switchFeatureFlag.setOnCheckedChangeListener(null)
            cbOverride.setOnCheckedChangeListener(null)
            cbOverride.isChecked = featureFlagValue.isOverride
            switchFeatureFlag.isEnabled = featureFlagValue.isOverride
            switchFeatureFlag.isChecked = if (featureFlagValue.isOverride) featureFlagValue.currentValue else featureFlagValue.remoteValue
            tvFeatureFlagName.text = featureFlagValue.featureFlag.name
            tvFeatureRemoteValue.text = featureFlagValue.remoteValue.toString()

            setupChangeListeners(featureFlagValueChangedListenerListener, featureFlagValue)
        }
    }

    private fun ItemFeatureFlagBinding.setupChangeListeners(
        featureFlagValueChangedListenerListener: FeatureFlagValueChangedListener,
        featureFlagValue: FeatureFlagValue
    ) {
        switchFeatureFlag.setOnCheckedChangeListener { _, isChecked ->
            featureFlagValueChangedListenerListener.onFeatureFlagValueChanged(
                featureFlagValue.featureFlag,
                isChecked
            )
        }

        cbOverride.setOnCheckedChangeListener { _, isChecked ->
            featureFlagValueChangedListenerListener.onOverrideValueChange(
                featureFlagValue.featureFlag,
                isChecked,
                featureFlagValue.remoteValue
            )
        }
    }
}