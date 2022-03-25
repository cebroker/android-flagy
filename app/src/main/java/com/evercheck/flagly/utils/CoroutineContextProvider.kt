package com.evercheck.flagly.utils

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class CoroutineContextProvider @Inject constructor (
    val mainDispatcher: CoroutineDispatcher,
    val backgroundDispatcher: CoroutineDispatcher
)