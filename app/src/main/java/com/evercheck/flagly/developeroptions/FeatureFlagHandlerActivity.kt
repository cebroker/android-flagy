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
import javax.inject.Inject

class FeatureFlagHandlerActivity : AppCompatActivity(), FeatureFlagActivityContract.View {

    @Inject
    lateinit var presenter: FeatureFlagActivityContract.Presenter
    private lateinit var binding: ActivityFeatureFlagHandlerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeatureFlagHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDependencyInjection()

        presenter.bind(this)
        presenter.onViewReady()
        presenter.onLoadFeatureFlagValues()

        binding.setUpSearchView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun ActivityFeatureFlagHandlerBinding.setUpSearchView() {
        svFeatureFlag.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?)  = false

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.onLoadFeatureFlagValues(newText ?: "")
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

    override fun onDestroy() {
        presenter.unBind()
        super.onDestroy()
    }

    override fun setup() {
        with(binding.rvFlags) {
            layoutManager = LinearLayoutManager(context)
            adapter = FeatureFlagAdapter(presenter)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    override fun showReatureFlagValues(featureFlagValues: List<FeatureFlagValue>) {
        binding.getFeatureFlagAdapter()
            .submitList(featureFlagValues, shouldSaveListToBeFiltered = true)
    }

    private fun ActivityFeatureFlagHandlerBinding.getFeatureFlagAdapter(): FeatureFlagAdapter {
        return rvFlags.adapter as FeatureFlagAdapter
    }
}
