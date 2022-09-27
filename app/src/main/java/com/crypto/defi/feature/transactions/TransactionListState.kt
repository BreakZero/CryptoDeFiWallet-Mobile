package com.crypto.defi.feature.transactions

import androidx.paging.PagingData
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.domain.EvmTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TransactionListState(
    val asset: Asset? = null,
    val transactionList: Flow<PagingData<EvmTransaction>> = flow { emit(PagingData.empty()) }
)