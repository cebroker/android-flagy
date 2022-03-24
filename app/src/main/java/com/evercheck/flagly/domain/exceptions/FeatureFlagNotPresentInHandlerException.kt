package com.evercheck.flagly.domain.exceptions

import com.evercheck.flagly.domain.model.FeatureFlag

class FeatureFlagNotPresentInHandlerException(featureFlag: FeatureFlag)
    : IllegalArgumentException("${featureFlag.name} is not present in feature flag handler")