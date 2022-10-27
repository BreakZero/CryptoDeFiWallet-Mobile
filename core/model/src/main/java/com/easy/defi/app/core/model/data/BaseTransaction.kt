package com.easy.defi.app.core.model.data

import java.math.BigInteger

open class BaseTransaction(
  val hash: String,
  val direction: TransactionDirection,
  val from: String,
  val to: String,
  val value: BigInteger,
  val timeStamp: String,
)

enum class TransactionDirection {
  SEND, RECEIVE, SEND_SELF
}
