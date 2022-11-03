package com.easy.defi.feature.asset.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.data.repository.CoinRepository
import com.easy.defi.app.core.data.repository.chain.ChainManager
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.feature.asset.transactions.navigation.TransactionListArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  stringDecoder: StringDecoder,
  coinRepository: CoinRepository,
  chainManager: ChainManager
) : ViewModel() {
  private val assetArgs: TransactionListArgs = TransactionListArgs(savedStateHandle, stringDecoder)

  fun slug() = assetArgs.slug

  val transactionListUiState = coinRepository.assetBySlug(assetArgs.slug)
    .filterNotNull().map {
      TransactionListUiState(
        asset = it,
        transactionPaging = chainManager.getChainByAsset(it)?.getTransactions(it.contract) ?: emptyFlow()
      )
    }.stateIn(viewModelScope, SharingStarted.Lazily, TransactionListUiState())
}

data class TransactionListUiState(
  val asset: Asset? = null,
  val transactionPaging: Flow<PagingData<BaseTransaction>> = emptyFlow()
)
