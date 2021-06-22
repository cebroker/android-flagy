package com.evercheck.flagly.utils

import kotlinx.coroutines.CoroutineDispatcher

class CoroutineContextProvider(
    val mainDispatcher: CoroutineDispatcher,
    val backgroundDispatcher: CoroutineDispatcher
)