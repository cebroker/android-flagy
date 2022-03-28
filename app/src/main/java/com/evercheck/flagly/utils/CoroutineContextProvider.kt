package com.evercheck.flagly.utils

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineContextProvider {
    val io: CoroutineDispatcher
}