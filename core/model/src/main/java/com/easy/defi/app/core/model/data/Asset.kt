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
  val rate: BigDecimal = BigDecimal.ZERO,
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
