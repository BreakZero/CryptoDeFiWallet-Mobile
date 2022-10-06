package com.crypto.defi.feature.nfts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.extensions.launchWithHandler
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.feature.nfts.group.NftGroupState
import com.crypto.defi.models.remote.nft.NftAssetGroup
import com.crypto.defi.models.remote.nft.BaseNftResponse
import com.crypto.defi.models.remote.nft.NftOwnerAssets
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NFTsViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    val ownerAssetState = MutableStateFlow(NFTsState())

    init {
        viewModelScope.launchWithHandler(Dispatchers.Default) {
            val response: BaseNftResponse<NftOwnerAssets> = client.get {
                url("${UrlConstant.NFT_SCAN_URL}/account/own/0x30145d714db337606c8f520bee9a3e3eac910636")
                header("X-API-KEY", "")
                parameter("erc_type", "erc721")
                parameter("limit", 20)
            }.body()
            ownerAssetState.update {
                NFTsState(isLoading = false, nfts = response.data.content)
            }
        }
    }
}