package com.crypto.defi.feature.assets.transactions

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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val chainManager: ChainManager,
    private val assetUseCase: AssetUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var txnState: TransactionListState = TransactionListState(
        asset = null, transactionList = flow { emit(PagingData.empty()) })
        private set

    fun init(slug: String) {
        if (savedStateHandle.get<String>("slug") == slug) return
        savedStateHandle["slug"] = slug
        assetUseCase.findAssetBySlug(slug)
            .map {
                txnState = it?.let { asset ->
                    val iChain = chainManager.getChainByKey(asset.code)
                    TransactionListState(
                        asset = it,
                        address = iChain.address(),
                        transactionList = Pager(PagingConfig(pageSize = 20)) {
                            TransactionListSource(
                                contractAddress = asset.contract,
                                iChain = iChain
                            )
                        }.flow.cachedIn(viewModelScope)
                    )
                } ?: TransactionListState(
                    asset = null,
                    transactionList = flow { emit(PagingData.empty()) }
                )
            }.launchIn(viewModelScope)
    }
}