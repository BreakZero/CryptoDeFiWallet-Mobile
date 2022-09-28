package com.crypto.defi.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.BalanceUseCase
import com.crypto.wallet.WalletRepository
import javax.inject.Inject

class DeFiWorkerFactory @Inject constructor(
    private val chainManager: ChainManager,
    private val balanceUseCase: BalanceUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = BalanceWorker(appContext, workerParameters, chainManager, balanceUseCase)
}