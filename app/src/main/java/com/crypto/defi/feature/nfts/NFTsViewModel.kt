package com.crypto.defi.feature.nfts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.remote.nft.AssetGroup
import com.crypto.defi.models.remote.nft.BaseNftResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NFTsViewModel @Inject constructor(
    private val client: HttpClient
): ViewModel() {
    val assetsByGroup = MutableStateFlow(NFTsState())
    init {
        viewModelScope.launch {
            val response: BaseNftResponse<List<AssetGroup>> = client.get {
                url("${UrlConstant.NFT_SCAN_URL}/account/own/all/0x30145d714db337606c8f520bee9a3e3eac910636")
                header("X-API-KEY","5nCEwFbKBh1shcolphAfBY4l")
                parameter("erc_type", "erc721")
                parameter("show_attribute", true)
            }.body()
            assetsByGroup.update {
                NFTsState(isLoading = false, ntfs = response.data)
            }
        }
    }
}