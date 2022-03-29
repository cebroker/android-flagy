package com.evercheck.flagly.developeroptions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.evercheck.flagly.databinding.ItemFeatureFlagBinding
import com.evercheck.flagly.developeroptions.FeatureFlagValue

class FeatureFlagAdapter(
    private val featureFlagValueChangedListener: FeatureFlagValueChangedListener
) : ListAdapter<FeatureFlagValue, FeatureFlagHandlerViewHolder>(object :
    DiffUtil.ItemCallback<FeatureFlagValue>() {

    override fun areItemsTheSame(oldItem: FeatureFlagValue, newItem: FeatureFlagValue): Boolean =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: FeatureFlagValue, newItem: FeatureFlagValue): Boolean =
        newItem == oldItem
}) {

    private var originalList: List<FeatureFlagValue> = arrayListOf()
    private var temporalList: List<FeatureFlagValue> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeatureFlagHandlerViewHolder {
        return FeatureFlagHandlerViewHolder(
            ItemFeatureFlagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeatureFlagHandlerViewHolder, position: Int) {
        holder.bind(getItem(position), featureFlagValueChangedListener)
    }

    fun submitList(list: List<FeatureFlagValue>?, shouldSaveListToBeFiltered: Boolean) {
        if (shouldSaveListToBeFiltered) {
            originalList = list as List<FeatureFlagValue>
        }
        super.submitList(list)
    }
}