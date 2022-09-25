package com.crypto.defi.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.crypto.defi.chains.ChainRepository
import com.crypto.defi.chains.usecase.BalanceUseCase
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.wallet.WalletRepository
import io.ktor.client.*
import javax.inject.Inject

class DeFiWorkerFactory @Inject constructor(
    private val walletRepository: WalletRepository,
    private val balanceUseCase: BalanceUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = BalanceWorker(appContext, workerParameters, walletRepository, balanceUseCase)
}