package com.crypto.defi.feature.nfts.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.extensions.launchWithHandler
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.feature.nfts.NFTsState
import com.crypto.defi.models.remote.nft.BaseNftResponse
import com.crypto.defi.models.remote.nft.NftAssetGroup
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
class NftGroupViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    val assetsByGroup = MutableStateFlow(NftGroupState())

    init {
        getNftGroupByErcType("erc721")
    }

    fun getNftGroupByErcType(ercType: String) {
        viewModelScope.launchWithHandler(Dispatchers.Default) {
            val response: BaseNftResponse<List<NftAssetGroup>> = client.get {
                url("${UrlConstant.NFT_SCAN_URL}/account/own/all/0x30145d714db337606c8f520bee9a3e3eac910636")
                header("X-API-KEY", "")
                parameter("erc_type", ercType)
                parameter("show_attribute", true)
            }.body()
            assetsByGroup.update {
                NftGroupState(isLoading = false, nftGroups = response.data)
            }
        }
    }
}