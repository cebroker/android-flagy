package com.evercheck.flagly.featurepoint

import com.evercheck.flagly.featurepoint.SuspendFeatureFactory
import com.evercheck.flagly.featurepoint.SuspendFeaturePoint

abstract class AbstractSuspendFeaturePoint<Feature, Params>(override val factories: List<SuspendFeatureFactory<Feature, Params>>) :
    SuspendFeaturePoint<Feature, Params> {

    override suspend fun createFeatures(params: Params): List<Feature> = factories.filter {
        it.isApplicable(params)
    }.map {
        it.create()
    }
}