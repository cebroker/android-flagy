package com.evercheck.flagly.developeroptions

import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.evercheck.flagly.databinding.ActivityFeatureFlagHandlerBinding
import com.evercheck.flagly.developeroptions.adapter.FeatureFlagAdapter
import com.evercheck.flagly.di.DaggerFeatureHandlerComponent
import com.evercheck.flagly.utils.EMPTY
import javax.inject.Inject

class FeatureFlagHandlerActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: FeatureFlagHandlerViewModel
    private lateinit var binding: ActivityFeatureFlagHandlerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeatureFlagHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDependencyInjection()
        setup()

        viewModel.filterFeatureFlagsByName()
        viewModel.featureFlagState.observe(this) { state ->
            binding.getFeatureFlagAdapter()
                .submitList(state.featureFlagValues, shouldSaveListToBeFiltered = true)
        }
        binding.setUpSearchView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun ActivityFeatureFlagHandlerBinding.setUpSearchView() {
        svFeatureFlag.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterFeatureFlagsByName(newText ?: EMPTY)
                return false
            }
        })
    }

    private fun setupDependencyInjection() {
        val featureHandlerResourceProvider = application as FeatureHandleResourceProvider
        DaggerFeatureHandlerComponent.factory()
            .create(
                featureHandlerResourceProvider.getFeatureFlagProvider(),
                featureHandlerResourceProvider.getRemoteFeatureFlagHandler(),
                featureHandlerResourceProvider.getLocalFeatureflagHandler()
            ).inject(this)
    }

    private fun setup() {
        with(binding.rvFlags) {
            layoutManager = LinearLayoutManager(context)
            adapter = FeatureFlagAdapter(viewModel)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun ActivityFeatureFlagHandlerBinding.getFeatureFlagAdapter(): FeatureFlagAdapter {
        return rvFlags.adapter as FeatureFlagAdapter
    }
}
