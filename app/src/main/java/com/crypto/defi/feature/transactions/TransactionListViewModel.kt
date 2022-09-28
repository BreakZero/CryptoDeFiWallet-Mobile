package com.crypto.defi.feature.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val chainManager: ChainManager,
    private val assetUseCase: AssetUseCase,
    val client: HttpClient,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun init(slug: String) {
        savedStateHandle["slug"] = slug
    }

    private val _slugState = savedStateHandle.getStateFlow("slug", "")

    val tnxState = _slugState.flatMapMerge {
        assetUseCase.findAssetBySlug(it)
    }.map {
        it?.let {
            val address = chainManager.getChainByKey(it.code).address()
            TransactionListState(
                asset = it,
                transactionList = Pager(PagingConfig(pageSize = 20)) {
                    TransactionListSource(
                        client = client,
                        contractAddress = it.contract,
                        address = address
                    )
                }.flow.cachedIn(viewModelScope)
            )
        } ?: TransactionListState(
            asset = null,
            transactionList = flow { emit(PagingData.empty()) }
        )
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())
}