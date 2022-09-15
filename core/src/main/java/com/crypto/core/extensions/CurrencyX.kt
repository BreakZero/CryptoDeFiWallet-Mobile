package com.crypto.core.extensions

import java.math.BigInteger
import java.math.RoundingMode

fun BigInteger.byDecimal(
    decimal: Int,
    display: Int = 8
): String {
    return this.toBigDecimal()
        .movePointLeft(decimal)
        .setScale(display, RoundingMode.DOWN)
        .toPlainString()
}
