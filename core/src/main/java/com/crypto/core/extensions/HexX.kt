package com.crypto.core.extensions

import com.crypto.core.common.hex
import com.crypto.core.common.shex
import com.crypto.core.common.unhex
import java.math.BigInteger

fun ByteArray.toHex(withPrefix: Boolean = false): String {
    return if (withPrefix) "0x${this.hex}" else this.hex
}

fun String.toHexBytes(): ByteArray {
    return this.unhex
}

fun Long.toHexByteArray(): ByteArray {
    return this.toString(16).unhex
}

fun Int.toHexByteArray(): ByteArray {
    return this.shex.unhex
}

fun BigInteger.toHexByteArray(): ByteArray {
    return this.toString(16).unhex
}
