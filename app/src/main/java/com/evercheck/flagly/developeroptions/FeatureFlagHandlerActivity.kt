package com.evercheck.flagly.developeroptions

import android.os.Bundle
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

        setupDependencyInjection()

        presenter.bind(this)
        presenter.onViewReady()
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
        (binding.rvFlags.adapter as FeatureFlagAdapter).submitList(featureFlagValues)
    }
}