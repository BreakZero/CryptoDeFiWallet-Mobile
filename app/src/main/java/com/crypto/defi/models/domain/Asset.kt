package com.crypto.defi.models.domain

import com.crypto.core.extensions.byDecimal
import com.crypto.defi.common.BigDecimalSerializer
import com.crypto.defi.common.BigIntegerSerializer
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@kotlinx.serialization.Serializable
data class Asset(
    val slug: String,
    val code: String,
    val iconUrl: String,
    val name: String,
    val symbol: String,
    val decimal: Int,
    val chainName: String,
    val contract: String? = null,
    @kotlinx.serialization.Serializable(with = BigIntegerSerializer::class)
    val nativeBalance: BigInteger = BigInteger.ZERO,
    @kotlinx.serialization.Serializable(with = BigDecimalSerializer::class)
    val rate: BigDecimal = BigDecimal.ZERO
) {
    fun fiatBalance(): BigDecimal {
        return nativeBalance.byDecimal(decimal).times(rate).setScale(2, RoundingMode.DOWN)
    }
}
