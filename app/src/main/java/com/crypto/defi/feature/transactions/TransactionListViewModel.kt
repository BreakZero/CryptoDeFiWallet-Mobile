package com.crypto.defi.feature.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.crypto.core.extensions.launchWithHandler
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val chainManager: ChainManager,
    private val assetUseCase: AssetUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var txnState: TransactionListState = TransactionListState(null, flow { emit(PagingData.empty()) })
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