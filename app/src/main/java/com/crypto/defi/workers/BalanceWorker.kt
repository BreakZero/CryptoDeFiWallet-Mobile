package com.crypto.defi.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.crypto.core.extensions.launchWithHandler
import com.crypto.defi.chains.usecase.BalanceUseCase
import com.crypto.defi.feature.assets.MainAssetsViewModel
import com.crypto.wallet.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType

class BalanceWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val walletRepository: WalletRepository,
    private val balanceUseCase: BalanceUseCase
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        setProgress(Data.Builder().put(MainAssetsViewModel.KEY_WORKER_PROGRESS, true).build())
        supervisorScope {
            val evmAddress = walletRepository.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
            launchWithHandler(Dispatchers.Default) {
                balanceUseCase.fetchingTiers()
            }
            launchWithHandler(Dispatchers.Default) {
                balanceUseCase.fetchingTokenHolding(
                    address = evmAddress
                )
            }
            launchWithHandler(Dispatchers.Default) {
                balanceUseCase.fetchingEthMainCoin(address = evmAddress)
            }
        }
        setProgress(Data.Builder().put(MainAssetsViewModel.KEY_WORKER_PROGRESS, false).build())
        return Result.success()
    }
}