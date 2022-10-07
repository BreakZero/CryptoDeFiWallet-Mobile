package com.crypto.defi.feature.assets.transactions

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crypto.defi.chains.IChain
import com.crypto.defi.models.domain.BaseTransaction

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