package com.crypto.defi.feature.assets.send

import com.crypto.defi.models.domain.Asset
import java.math.BigInteger

data class SendFormState(
    val asset: Asset? = null,
    val to: String = "",
    val amount: String = "",
    val memo: String = "",
    val plan: TransactionPlan = TransactionPlan.EmptyPlan
)

data class ReadyToSign(
    val to: String,
    val amount: BigInteger,
    val memo: String? = null,
    val contract: String? = null,
    val chainId: Int = 3
)

data class TransactionPlan(
    val rawData: String,
    val action: String,
    val amount: BigInteger,
    val to: String,
    val from: String,
    val fee: BigInteger
) {
  companion object {
    val EmptyPlan = TransactionPlan("", "", BigInteger.ZERO, "", "", BigInteger.ZERO)
  }

  fun isEmptyPlan(): Boolean {
    return this == EmptyPlan
  }
}