package com.crypto.defi.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.crypto.defi.chains.ChainRepository
import com.crypto.defi.models.local.CryptoDeFiDatabase
import io.ktor.client.*
import javax.inject.Inject

class DeFiWorkerFactory @Inject constructor(
    private val chainRepository: ChainRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = BalanceWorker(appContext, workerParameters, chainRepository)
}