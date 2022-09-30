package com.crypto.defi.feature.transactions

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crypto.defi.chains.IChain
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.BaseTransaction
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.mapper.toEvmTransaction
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.EvmTransactionDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TransactionListSource(
    private val iChain: IChain,
    private val contractAddress: String?
) : PagingSource<Int, BaseTransaction>() {
    override fun getRefreshKey(state: PagingState<Int, BaseTransaction>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BaseTransaction> {
        return try {
            val nextPage = params.key ?: 1
            val transactionList = iChain.transactions(
                nextPage,
                20,
                contract = contractAddress
            )
            LoadResult.Page(
                data = transactionList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (transactionList.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}