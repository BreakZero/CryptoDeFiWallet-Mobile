package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.network.EthereumDataSource
import java.math.BigInteger
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EvmChainRepository @Inject constructor(
  private val ethereumDataSource: EthereumDataSource,
) : ChainRepository {
  override fun getBalanceStream(address: String, contractAddress: String?): Flow<BigInteger> {
    return flow { emit(ethereumDataSource.getSingleBalance(address, contractAddress)) }
  }

  override fun getTransactions(
    address: String,
    page: Int,
    offset: Int,
    contractAddress: String?,
  ): Flow<List<BaseTransaction>> {
    return flow { emit(ethereumDataSource.getTransactions(address, page, offset, contractAddress)) }
  }

  override fun broadcastTransaction(rawData: String): Flow<String> {
    return flow { emit(ethereumDataSource.broadcast(rawData)) }
  }
}
