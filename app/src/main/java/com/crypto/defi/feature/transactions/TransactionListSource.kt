package com.crypto.defi.feature.transactions

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.mapper.toEvmTransaction
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.EvmTransactionDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TransactionListSource(
    private val client: HttpClient,
    private val address: String,
    private val contractAddress: String?
) : PagingSource<Int, EvmTransaction>() {
    override fun getRefreshKey(state: PagingState<Int, EvmTransaction>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EvmTransaction> {
        return try {
            val nextPage = params.key ?: 1
            val transactionList = client.get(
                urlString = "${UrlConstant.BASE_URL}/ethereum/transactions/$address"
            ) {
                parameter("page", nextPage)
                parameter("offset", params.loadSize)
                contractAddress?.also {
                    parameter("contract", it)
                }
            }.body<BaseResponse<List<EvmTransactionDto>>>().data
            LoadResult.Page(
                data = transactionList.map {
                    it.toEvmTransaction(address)
                },
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (transactionList.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}