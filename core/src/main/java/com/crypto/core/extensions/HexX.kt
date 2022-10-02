package com.crypto.core.extensions

import com.crypto.core.common.Numeric
import java.math.BigInteger

fun ByteArray.toHex(withPrefix: Boolean = false): String {
    return Numeric.toHexString(this)
}

fun String.toHexBytes(): ByteArray {
    return Numeric.hexStringToByteArray(this)
}

fun Long.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this.toString(16))
}

fun Int.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this.toString(16))
}

fun BigInteger.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this.toString(16))
}
