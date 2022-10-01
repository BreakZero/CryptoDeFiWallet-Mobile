package com.crypto.defi.feature.assets.send

import java.math.BigInteger

data class SendFormInfo(
    val to: String = "",
    val memo: String = "",
    val amount: String = ""
)

data class SendFormState(
    val symbol: String = "",
    val formInfo: SendFormInfo = SendFormInfo(),
    val plan: TransactionPlan = TransactionPlan.EmptyPlan
)

data class ReadyToSign(
    val to: String,
    val amount: BigInteger,
    val memo: String,
)

data class TransactionPlan(
    val action: String,
    val amount: String,
    val symbol: String,
    val to: String,
    val from: String,
    val fee: String
) {
    companion object {
        val EmptyPlan = TransactionPlan("","","","","","")
    }

    fun isEmptyPlan(): Boolean {
        return this == EmptyPlan
    }
}