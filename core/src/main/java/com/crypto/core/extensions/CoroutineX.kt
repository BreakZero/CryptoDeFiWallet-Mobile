package com.crypto.core.extensions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private val handler = CoroutineExceptionHandler { _, exception ->
  Timber.e(exception)
}

fun CoroutineScope.launchWithHandler(
  context: CoroutineContext = EmptyCoroutineContext,
  block: suspend CoroutineScope.() -> Unit
) {
  launch(context + handler) {
    block()
  }
}