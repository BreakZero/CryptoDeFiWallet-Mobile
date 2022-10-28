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
import com.easy.defi.app.core.data.repository.EvmChainRepository
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
  private val evmChainRepository: EvmChainRepository,
  @Dispatcher(DwDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

  override suspend fun getForegroundInfo(): ForegroundInfo =
    appContext.syncForegroundInfo()

  override suspend fun doWork(): Result = withContext(ioDispatcher) {
    traceAsync("Sync", 0) {
      // First sync the repositories in parallel
      val syncedSuccessfully = awaitAll(
        async { evmChainRepository.sync() },
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
