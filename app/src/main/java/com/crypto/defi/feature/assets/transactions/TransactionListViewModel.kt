package com.crypto.defi.feature.assets.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class TransactionListViewModel @AssistedInject constructor(
    private val chainManager: ChainManager,
    assetUseCase: AssetUseCase,
    @Assisted private val coinSlug: String
) : ViewModel() {
    private val _txnState = assetUseCase.findAssetBySlug(coinSlug).filterNotNull().map { asset ->
        val iChain = chainManager.getChainByKey(asset.code)
        TransactionListState(
            asset = asset,
            address = iChain.address(),
            transactionList = Pager(PagingConfig(pageSize = 20)) {
                TransactionListSource(
                    contractAddress = asset.contract, iChain = iChain
                )
            }.flow.cachedIn(viewModelScope)
        )
    }

    val txnState = _txnState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TransactionListState(
            asset = null,
            transactionList = flow { emit(PagingData.empty()) }
        )
    )

    fun coinSlug() = coinSlug
}