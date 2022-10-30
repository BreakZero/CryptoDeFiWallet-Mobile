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

package com.easy.defi.app.core.model.data

import com.easy.defi.app.core.model.x.byDecimal
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

data class Asset(
  val slug: String,
  val code: String,
  val iconUrl: String,
  val name: String,
  val symbol: String,
  val decimal: Int,
  val chainName: String,
  val contract: String? = null,
  val nativeBalance: BigInteger = BigInteger.ZERO,
  val rate: BigDecimal = BigDecimal.ZERO
) {
  fun fiatBalance(): BigDecimal {
    return nativeBalance.byDecimal(decimal).times(rate).setScale(2, RoundingMode.DOWN)
  }

  fun feeDecimal(): Int {
    return contract?.let {
      18
    } ?: decimal
  }

  fun feeSymbol(): String {
    return contract?.let {
      "ETH"
    } ?: symbol
  }
}
