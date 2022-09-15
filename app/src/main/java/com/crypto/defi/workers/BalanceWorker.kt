package com.crypto.defi.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.crypto.defi.chains.ChainRepository
import com.crypto.defi.feature.assets.MainAssetsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext

class BalanceWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val chainRepository: ChainRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        setProgress(Data.Builder().put(MainAssetsViewModel.KEY_WORKER_PROGRESS, true).build())
        withContext(Dispatchers.IO) {
            chainRepository.tokenHoldings().onEach {
                chainRepository.updateBalance(it.contractAddress, it.amount)
            }
        }
        setProgress(Data.Builder().put(MainAssetsViewModel.KEY_WORKER_PROGRESS, false).build())
        return Result.success()
    }
}