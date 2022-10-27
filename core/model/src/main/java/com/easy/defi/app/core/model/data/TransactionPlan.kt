package com.easy.defi.app.core.model.data

import java.math.BigInteger

data class TransactionPlan(
  val rawData: String,
  val action: String,
  val amount: BigInteger,
  val to: String,
  val from: String,
  val fee: BigInteger,
) {
  companion object {
    val EmptyPlan = TransactionPlan("", "", BigInteger.ZERO, "", "", BigInteger.ZERO)
  }

  fun isEmptyPlan(): Boolean {
    return this == EmptyPlan
  }
}
