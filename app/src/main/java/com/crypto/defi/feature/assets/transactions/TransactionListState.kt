package com.crypto.defi.feature.assets.transactions

import androidx.paging.PagingData
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.domain.BaseTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TransactionListState(
    val asset: Asset? = null,
    val address: String = "",
    val transactionList: Flow<PagingData<BaseTransaction>> = flow { emit(PagingData.empty()) }
)
