package com.crypto.defi.models.domain

import java.math.BigInteger

data class EvmTransaction(
    val hash: String,
    val direction: TransactionDirection,
    val from: String,
    val to: String,
    val input: String,
    val gas: BigInteger,
    val gasPrice: BigInteger,
    val value: BigInteger,
    val timeStamp: String
)

enum class TransactionDirection {
    SEND, RECEIVE, SEND_SELF
}
