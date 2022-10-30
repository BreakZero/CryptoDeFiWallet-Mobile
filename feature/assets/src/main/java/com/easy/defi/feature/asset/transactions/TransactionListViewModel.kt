package com.easy.defi.feature.asset.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.easy.defi.app.core.data.di.annotations.Ethereum
import com.easy.defi.app.core.data.repository.ChainRepository
import com.easy.defi.app.core.data.repository.CoinRepository
import com.easy.defi.app.core.model.data.BaseTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
  coinRepository: CoinRepository,
  @Ethereum chainRepository: ChainRepository
) : ViewModel() {

  val transactionListUiState = coinRepository.assetBySlug("ethereum").map {
    TransactionListUiState.Success(
      transactionPaging = chainRepository.getTransactions(it?.contract)
    )
  }.stateIn(viewModelScope, SharingStarted.Lazily, TransactionListUiState.Loading)

  fun coinSlug() = "ethereum"
}

sealed interface TransactionListUiState {
  object Loading : TransactionListUiState
  object Error : TransactionListUiState
  data class Success(val transactionPaging: Flow<PagingData<BaseTransaction>>)
}
