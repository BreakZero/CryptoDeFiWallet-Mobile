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

import com.easy.defi.app.core.data.Synchronizer
import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.network.EthereumDataSource
import java.math.BigInteger
import javax.inject.Inject
import kotlinx.coroutines.delay
import timber.log.Timber

class EvmChainRepository @Inject constructor(
  private val ethereumDataSource: EthereumDataSource,
) : ChainRepository {
  override suspend fun getBalance(address: String, contractAddress: String?): BigInteger {
    return ethereumDataSource.getSingleBalance(address, contractAddress)
  }

  override suspend fun getTransactions(
    address: String,
    page: Int,
    offset: Int,
    contractAddress: String?,
  ): List<BaseTransaction> {
    return ethereumDataSource.getTransactions(address, page, offset, contractAddress)
  }

  override suspend fun broadcastTransaction(rawData: String): String {
    return ethereumDataSource.broadcast(rawData)
  }

  override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
    Timber.tag("=====").v("Hello work, ${System.currentTimeMillis()}")
    delay(1000)
    Timber.tag("=====").v("Hello work, ${System.currentTimeMillis()}")
    return true
  }
}
