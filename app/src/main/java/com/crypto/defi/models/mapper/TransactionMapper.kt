package com.crypto.defi.models.mapper

import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.domain.TransactionDirection
import com.crypto.defi.models.remote.EvmTransactionDto
import kotlinx.datetime.Instant

fun EvmTransactionDto.toEvmTransaction(
    owner: String
): EvmTransaction {
    val direction = if (this.from.equals(owner, true))
        TransactionDirection.RECEIVE else TransactionDirection.SEND

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
        timeStamp = Instant.fromEpochSeconds(timestamp).toString()
    )
}
