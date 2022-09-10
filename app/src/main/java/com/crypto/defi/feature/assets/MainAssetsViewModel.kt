package com.crypto.defi.feature.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.crypto.core.common.UiText
import com.crypto.core.model.NetworkStatus
import com.crypto.defi.chains.ChainRepository
import com.crypto.defi.workers.BalanceWorker
import com.crypto.resource.R
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAssetsViewModel @Inject constructor(
    private val chainRepository: ChainRepository,
    private val client: HttpClient,
    private val workManager: WorkManager
) : ViewModel() {
    var assetState by mutableStateOf(MainAssetState(
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
    ))
        private set

    init {
        val balanceRequestWorker = OneTimeWorkRequestBuilder<BalanceWorker>().build()
        workManager.enqueue(balanceRequestWorker)
        viewModelScope.launch(Dispatchers.IO) {
            chainRepository.fetching()

            chainRepository.assetsFlow().collect {
                assetState = assetState.copy(
                    onRefreshing = false, assetsResult = NetworkStatus.Success(it)
                )
            }
        }
    }
}
