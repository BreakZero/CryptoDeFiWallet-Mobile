package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.model.data.BaseTransaction
import java.math.BigInteger
import kotlinx.coroutines.flow.Flow

interface ChainRepository {
  fun getBalanceStream(
    address: String,
    contractAddress: String?,
  ): Flow<BigInteger>

  fun getTransactions(
    address: String,
    page: Int,
    offset: Int,
    contractAddress: String?,
  ): Flow<List<BaseTransaction>>

  fun broadcastTransaction(rawData: String): Flow<String>
}
