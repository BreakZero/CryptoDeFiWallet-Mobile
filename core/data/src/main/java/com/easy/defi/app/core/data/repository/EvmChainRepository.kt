/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.data.HdWalletHolder
import com.easy.defi.app.core.data.Synchronizer
import com.easy.defi.app.core.database.dao.AssetDao
import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.network.EthereumDataSource
import timber.log.Timber
import wallet.core.jni.CoinType
import java.math.BigInteger
import javax.inject.Inject

class EvmChainRepository @Inject constructor(
  private val hdWalletHolder: HdWalletHolder,
  private val ethereumDataSource: EthereumDataSource,
  private val assetDao: AssetDao
) : ChainRepository {

  override val address: String?
    get() = hdWalletHolder.hdWallet?.getAddressForCoin(CoinType.ETHEREUM)

  override suspend fun getBalance(contractAddress: String?): BigInteger {
    return address?.let {
      ethereumDataSource.getSingleBalance(it, contractAddress)
    } ?: BigInteger.ZERO
  }

  override suspend fun getTransactions(
    page: Int,
    offset: Int,
    contractAddress: String?
  ): List<BaseTransaction> {
    return address?.let {
      ethereumDataSource.getTransactions(it, page, offset, contractAddress)
    } ?: emptyList()
  }

  override suspend fun broadcastTransaction(rawData: String): String {
    return ethereumDataSource.broadcast(rawData)
  }

  override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
    Timber.tag("=====").v(address)
    return address?.let {
      val ethBalance = ethereumDataSource.getSingleBalance(it, null)
      assetDao.updateBalanceForMainChain("eth", ethBalance.toString())
      val tokenHoldings = ethereumDataSource.getTokenHoldings(it)
      tokenHoldings.onEach {
        assetDao.updateBalance(contract = it.contractAddress, balance = it.amount)
      }
      true
    } ?: false
  }
}
