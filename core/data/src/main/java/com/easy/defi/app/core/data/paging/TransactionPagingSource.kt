package com.easy.defi.app.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.network.EthereumDataSource

class TransactionPagingSource(
  private val address: String?,
  private val contractAddress: String?,
  private val ethereumDataSource: EthereumDataSource
) : PagingSource<Int, BaseTransaction>() {
  override fun getRefreshKey(state: PagingState<Int, BaseTransaction>): Int? {
    return state.anchorPosition
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BaseTransaction> {
    return address?.let {
      try {
        val nextPage = params.key ?: 1
        val transactions = ethereumDataSource.getTransactions(
          address = address,
          page = nextPage,
          offset = 20,
          contract = contractAddress
        )
        LoadResult.Page(
          data = transactions,
          prevKey = if (nextPage == 1) null else nextPage - 1,
          nextKey = if (transactions.isEmpty()) null else nextPage + 1
        )
      } catch (e: Exception) {
        LoadResult.Error(e)
      }
    } ?: LoadResult.Invalid()
  }
}
