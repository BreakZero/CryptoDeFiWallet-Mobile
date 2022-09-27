package com.crypto.defi.models.mapper

import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.domain.TransactionDirection
import com.crypto.defi.models.remote.EvmTransactionDto
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId

fun EvmTransactionDto.toEvmTransaction(
    owner: String
): EvmTransaction {
    val direction =
        if (this.from == owner) TransactionDirection.SEND else TransactionDirection.RECEIVE
    val timestamp = Timestamp(this.timeStamp.toLong().times(1000L))
    return EvmTransaction(
        hash = this.hash,
        direction = direction,
        from = this.from,
        to = this.to,
        input = this.input,
        gas = this.gas.clearHexPrefix().toBigInteger(16),
        gasPrice = this.gasPrice.clearHexPrefix().toBigInteger(16),
        value = this.value.toBigInteger(),
        timeStamp = Instant.ofEpochMilli(timestamp.time).atZone(ZoneId.systemDefault())
            .toLocalDateTime().toString()
    )
}
