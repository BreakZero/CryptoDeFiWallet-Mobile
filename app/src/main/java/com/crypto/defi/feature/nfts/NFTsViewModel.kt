package com.crypto.defi.feature.nfts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.remote.nft.NftAssetGroup
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
) : ViewModel() {

    init {

    }
}