package com.evercheck.flagly.developeroptions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.evercheck.flagly.databinding.ItemFeatureFlagBinding
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import java.util.*
import kotlin.collections.ArrayList

class FeatureFlagAdapter(
    private val featureFlagValueChangedListener: FeatureFlagValueChangedListener
) : ListAdapter<FeatureFlagValue, FeatureFlagHandlerViewHolder>(object :
    DiffUtil.ItemCallback<FeatureFlagValue>() {

    override fun areItemsTheSame(oldItem: FeatureFlagValue, newItem: FeatureFlagValue): Boolean =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: FeatureFlagValue, newItem: FeatureFlagValue): Boolean =
        newItem == oldItem
}), Filterable {

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


    var originalList = ArrayList<FeatureFlagValue>()
    var temporalList = ArrayList<FeatureFlagValue>()

    fun submitList(list: List<FeatureFlagValue>?, shouldSaveListToBeFiltered: Boolean) {
        if (shouldSaveListToBeFiltered) {
            originalList = list as ArrayList<FeatureFlagValue>
        }
        super.submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                temporalList = if (charSearch.isEmpty()) {
                    originalList
                } else {
                    val resultList = ArrayList<FeatureFlagValue>()
                    for (row in originalList) {
                        if (row.featureFlag.name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = temporalList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                temporalList = results?.values as ArrayList<FeatureFlagValue>
                submitList(temporalList, shouldSaveListToBeFiltered = false)
            }
        }
    }
}