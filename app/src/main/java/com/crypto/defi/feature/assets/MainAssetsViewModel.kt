package com.crypto.defi.feature.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.crypto.core.common.UiText
import com.crypto.core.model.NetworkStatus
import com.crypto.defi.chains.ChainRepository
import com.crypto.defi.workers.BalanceWorker
import com.crypto.resource.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainAssetsViewModel @Inject constructor(
    private val chainRepository: ChainRepository,
    private val workManager: WorkManager
) : ViewModel() {

    companion object {
        private const val WORKER_NAME = "update-balance-worker"
    }

    private val balanceWorkerRequest = PeriodicWorkRequestBuilder<BalanceWorker>(
        15, TimeUnit.MINUTES
    ).setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    ).build()

    var assetState by mutableStateOf(
        MainAssetState(
            promoCard = listOf(
                PromoCart(
                    backgroundRes = R.drawable.card_small_orange,
                    title = UiText.StringResource(R.string.new_coins__new_coin)
                ),
                PromoCart(
                    backgroundRes = R.drawable.card_small_black,
                    title = UiText.StringResource(R.string.wallet_asset__get_eth_ready_for_gas_fees)
                ),
                PromoCart(
                    backgroundRes = R.drawable.card_small_purple,
                    title = UiText.StringResource(R.string.wallet_asset__enable_email)
                )
            )
        )
    )
        private set

    init {
        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            balanceWorkerRequest
        )
        viewModelScope.launch(Dispatchers.IO) {
            chainRepository.fetching()

            chainRepository.assetsFlow().collect {
                assetState = assetState.copy(
                    onRefreshing = false, assetsResult = NetworkStatus.Success(it)
                )
            }
        }
    }

    fun onRefresh() {
        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            balanceWorkerRequest
        )
        viewModelScope.launch {
            assetState = assetState.copy(
                onRefreshing = true
            )
            delay(1000)
            assetState = assetState.copy(
                onRefreshing = false
            )
        }
    }
}
