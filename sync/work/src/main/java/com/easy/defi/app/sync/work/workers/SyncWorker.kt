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

package com.easy.defi.app.sync.work.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.easy.defi.app.core.common.network.Dispatcher
import com.easy.defi.app.core.common.network.DwDispatchers
import com.easy.defi.app.core.data.Synchronizer
import com.easy.defi.app.core.data.di.annotations.Bitcoin
import com.easy.defi.app.core.data.di.annotations.Ethereum
import com.easy.defi.app.core.data.repository.ChainRepository
import com.easy.defi.app.sync.work.initializers.SyncConstraints
import com.easy.defi.app.sync.work.initializers.syncForegroundInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
  @Assisted private val appContext: Context,
  @Assisted workerParams: WorkerParameters,
  @Ethereum private val evmChainRepository: ChainRepository,
  @Bitcoin private val bitcoinChainRepository: ChainRepository,
  @Dispatcher(DwDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParams), Synchronizer {

  override suspend fun getForegroundInfo(): ForegroundInfo =
    appContext.syncForegroundInfo()

  override suspend fun doWork(): Result = withContext(ioDispatcher) {
    traceAsync("Sync", 0) {
      // First sync the repositories in parallel
      val syncedSuccessfully = awaitAll(
        async { evmChainRepository.sync() },
        async { bitcoinChainRepository.sync() }
      ).all { it }

      if (syncedSuccessfully) {
        Result.success()
      } else {
        Result.retry()
      }
    }
  }

  companion object {
    /**
     * Expedited one time work to sync data on app startup
     */
    fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
      .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
      .setConstraints(SyncConstraints)
      .setInputData(SyncWorker::class.delegatedData())
      .build()
  }
}
