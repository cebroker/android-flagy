package com.evercheck.flagly.exceptions

import com.evercheck.flagly.data.model.FeatureFlag

class FeatureFlagNotPresentInHandlerException(featureFlag: FeatureFlag)
    : IllegalArgumentException("${featureFlag.name} is not present in feature flag handler")