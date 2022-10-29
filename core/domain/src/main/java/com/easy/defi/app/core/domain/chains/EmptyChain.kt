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

package com.easy.defi.app.core.domain.chains

import com.easy.defi.app.core.model.data.EvmTransaction
import com.easy.defi.app.core.model.data.ReadyToSign
import com.easy.defi.app.core.model.data.TransactionPlan
import java.math.BigInteger

class EmptyChain : IChain {
  override fun address(): String {
    return ""
  }

  override suspend fun balance(contract: String?): BigInteger {
    return BigInteger.ZERO
  }

  override suspend fun transactions(
    page: Int,
    offset: Int,
    contract: String?
  ): List<EvmTransaction> {
    return emptyList()
  }

  override suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan {
    return TransactionPlan.EmptyPlan
  }

  override suspend fun broadcast(rawData: String): String {
    return ""
  }
}
