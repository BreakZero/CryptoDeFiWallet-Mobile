package com.crypto.defi.feature.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.common.UiText
import com.crypto.core.model.NetworkStatus
import com.crypto.defi.chains.ChainRepository
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.remote.AssetDto
import com.crypto.defi.models.remote.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.crypto.resource.R

@HiltViewModel
class MainAssetsViewModel @Inject constructor(
    private val chainRepository: ChainRepository,
    private val client: HttpClient
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
        viewModelScope.launch(Dispatchers.IO) {
            chainRepository.fetching()
        }
        viewModelScope.launch(Dispatchers.IO) {
            assetState = try {
                val remoteAsset = client.get("http://192.168.1.105:8080/currencies")
                    .body<BaseResponse<AssetDto>>()
                    .data.currencies.map {
                        Asset(
                            slug = it.slug,
                            iconUrl = it.iconUrl,
                            name = it.coinName,
                            symbol = it.symbol,
                            code = it.chain
                        )
                    }
                assetState.copy(assetsResult = NetworkStatus.Success(remoteAsset.take(18)))
            } catch (e: Exception) {
                assetState.copy(assetsResult = NetworkStatus.Error("something went wrong"))
            }
        }
    }
}
