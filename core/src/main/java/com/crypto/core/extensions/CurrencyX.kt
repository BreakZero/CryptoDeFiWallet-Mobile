package com.crypto.core.extensions

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun BigInteger.byDecimal2String(
    decimal: Int,
    display: Int = 8
): String {
    return byDecimal(decimal, display)
        .toPlainString()
}

fun BigInteger.byDecimal(
    decimal: Int,
    display: Int = 8
): BigDecimal {
    return this.toBigDecimal()
        .movePointLeft(decimal)
        .setScale(display, RoundingMode.DOWN)
}

fun BigDecimal.upWithDecimal(
    decimal: Int
): BigInteger {
    return this.movePointRight(decimal).toBigInteger()
}
