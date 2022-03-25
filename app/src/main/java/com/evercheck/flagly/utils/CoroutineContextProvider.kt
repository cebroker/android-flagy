package com.evercheck.flagly.utils

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineContextProvider {
    val mainDispatcher: CoroutineDispatcher
    val backgroundDispatcher: CoroutineDispatcher
}