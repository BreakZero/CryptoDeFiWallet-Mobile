/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi.app.core.data

import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

/**
 * Interface marker for a class that manages synchronization between local data and a remote
 * source for a [Syncable].
 */
interface Synchronizer {
  /**
   * Syntactic sugar to call [Syncable.syncWith] while omitting the synchronizer argument
   */
  suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

/**
 * Interface marker for a class that is synchronized with a remote source. Syncing must not be
 * performed concurrently and it is the [Synchronizer]'s responsibility to ensure this.
 */
interface Syncable {
  /**
   * Synchronizes the local database backing the repository with the network.
   * Returns if the sync was successful or not.
   */
  suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

/**
 * Attempts [block], returning a successful [Result] if it succeeds, otherwise a [Result.Failure]
 * taking care not to break structured concurrency
 */
private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
  Result.success(block())
} catch (cancellationException: CancellationException) {
  throw cancellationException
} catch (exception: Exception) {
  Timber.tag("suspendRunCatching")
    .i(exception, "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result")
  Result.failure(exception)
}

/**
 * Utility function for syncing a repository with the network.
 *
 * Note that the blocks defined above are never run concurrently, and the [Synchronizer]
 * implementation must guarantee this.
 */
suspend fun Synchronizer.sync(
  block: suspend () -> Unit
) = suspendRunCatching {
  block()
}.isSuccess
