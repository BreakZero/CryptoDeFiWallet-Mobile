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

package com.easy.defi.app.core.data.repository.chain

import androidx.paging.PagingData
import com.easy.defi.app.core.data.Syncable
import com.easy.defi.app.core.model.data.BaseTransaction
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

interface ChainRepository : Syncable {
  val address: String?
  suspend fun getBalance(
    contractAddress: String?
  ): BigInteger

  suspend fun getTransactions(
    contractAddress: String?
  ): Flow<PagingData<BaseTransaction>>

  suspend fun broadcastTransaction(rawData: String): String
}
