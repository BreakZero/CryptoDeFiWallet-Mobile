package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.data.Syncable
import com.easy.defi.app.core.model.data.BaseTransaction
import java.math.BigInteger

interface ChainRepository : Syncable {
  suspend fun getBalance(
    address: String,
    contractAddress: String?,
  ): BigInteger

  suspend fun getTransactions(
    address: String,
    page: Int,
    offset: Int,
    contractAddress: String?,
  ): List<BaseTransaction>

  suspend fun broadcastTransaction(rawData: String): String
}
