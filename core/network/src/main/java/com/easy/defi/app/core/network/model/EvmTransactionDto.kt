package com.easy.defi.app.core.network.model

import com.easy.defi.app.core.common.extensions.clearHexPrefix
import com.easy.defi.app.core.model.data.EvmTransaction
import com.easy.defi.app.core.model.data.TransactionDirection
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvmTransactionDto(
  @SerialName("blockHash")
  val blockHash: String,
  @SerialName("blockNumber")
  val blockNumber: String,
  @SerialName("confirmations")
  val confirmations: String,
  @SerialName("contractAddress")
  val contractAddress: String,
  @SerialName("cumulativeGasUsed")
  val cumulativeGasUsed: String,
  @SerialName("from")
  val from: String,
  @SerialName("functionName")
  val functionName: String?,
  @SerialName("gas")
  val gas: String,
  @SerialName("gasPrice")
  val gasPrice: String,
  @SerialName("gasUsed")
  val gasUsed: String,
  @SerialName("hash")
  val hash: String,
  @SerialName("input")
  val input: String,
  @SerialName("isError")
  val isError: String?,
  @SerialName("methodId")
  val methodId: String?,
  @SerialName("nonce")
  val nonce: String,
  @SerialName("timeStamp")
  val timeStamp: String,
  @SerialName("to")
  val to: String,
  @SerialName("transactionIndex")
  val transactionIndex: String,
  @SerialName("txreceipt_status")
  val txreceiptStatus: String?,
  @SerialName("value")
  val value: String,
)

fun EvmTransactionDto.asExternalModel(
  owner: String,
): EvmTransaction {
  val direction = if (this.from.equals(owner, true)) {
    TransactionDirection.RECEIVE
  } else {
    TransactionDirection.SEND
  }

  val timestamp = this.timeStamp.toLong()
  return EvmTransaction(
    hash = this.hash,
    direction = direction,
    from = this.from,
    to = this.to,
    input = this.input,
    gas = this.gas.clearHexPrefix().toBigInteger(16),
    gasPrice = this.gasPrice.clearHexPrefix().toBigInteger(16),
    value = this.value.toBigInteger(),
    timeStamp = Instant.fromEpochSeconds(timestamp).toString(),
  )
}
