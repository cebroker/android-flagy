package com.evercheck.flagly.featurepoint

interface SuspendFeatureFactory<out Feature, in Params> {

    suspend fun create(): Feature

    suspend fun isApplicable(params: Params): Boolean
}