package com.crypto.defi.feature.nfts

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.AppSettingsConfig
import com.crypto.defi.models.remote.nft.BaseNftResponse
import com.crypto.defi.models.remote.nft.NftInfo
import com.crypto.defi.models.remote.nft.NftOwnerAssets
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NFTsViewModel @Inject constructor(
  private val client: HttpClient,
  private val appSettingsConfig: DataStore<AppSettingsConfig>
) : ViewModel() {
  private val _isLoadingFlow = MutableStateFlow(true)
  private val _nftsFlow = MutableStateFlow(emptyList<NftInfo>())

  init {
    viewModelScope.launch {
      _nftsFlow.update {
        loadNfts("")
      }
    }
  }

  val ownerAssetState = combine(_nftsFlow, appSettingsConfig.data, _isLoadingFlow) { nfts, appSettings, isLoading ->
    NFTsState(isLoading = isLoading, nfts = nfts, walletNameInfo = appSettings.walletNameInfo)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NFTsState())

  fun refresh() {
    viewModelScope.launch {
      _nftsFlow.update {
        loadNfts("5nCEwFbKBh1shcolphAfBY4l")
      }
    }
  }

  private suspend fun loadNfts(apiKey: String = ""): List<NftInfo> {
    _isLoadingFlow.update { true }
    val nfts = try {
      val response: BaseNftResponse<NftOwnerAssets> = client.get {
        url("${UrlConstant.NFT_SCAN_URL}/account/own/0x30145d714db337606c8f520bee9a3e3eac910636")
        header("X-API-KEY", apiKey)
        parameter("erc_type", "erc721")
        parameter("limit", 20)
      }.body()
      response.data.content
    } catch (e: Exception) {
      emptyList()
    }
    _isLoadingFlow.update { false }
    return nfts
  }
}