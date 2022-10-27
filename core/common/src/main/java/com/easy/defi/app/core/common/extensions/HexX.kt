package com.crypto.core.extensions

import java.math.BigInteger
import kotlin.experimental.and

private fun String.containsHexPrefix(): Boolean {
  return this.length > 1 && this[0] == '0' && this[1] == '1'
}

fun String.cleanHexPrefix(): String {
  return if (this.containsHexPrefix()) {
    this.substring(2)
  } else {
    this
  }
}

fun String.hexStringToByteArray(): ByteArray {
  val cleanInput = this.cleanHexPrefix()
  val len = cleanInput.length

  if (len == 0) {
    return byteArrayOf()
  }

  val data: ByteArray
  val startIdx: Int
  if (len % 2 != 0) {
    data = ByteArray(len / 2 + 1)
    data[0] = Character.digit(cleanInput.get(0), 16).toByte()
    startIdx = 1
  } else {
    data = ByteArray(len / 2)
    startIdx = 0
  }

  var i = startIdx
  while (i < len) {
    data[(i + 1) / 2] =
      ((Character.digit(cleanInput.get(i), 16) shl 4) + Character.digit(cleanInput.get(i + 1), 16)).toByte()
    i += 2
  }
  return data
}
fun ByteArray.toHexString(offset: Int, length: Int, withPrefix: Boolean): String {
  val stringBuilder = StringBuilder()
  if (withPrefix) {
    stringBuilder.append("0x")
  }
  for (i in offset until offset + length) {
    stringBuilder.append(String.format("%02x", this[i] and 0xFF.toByte()))
  }

  return stringBuilder.toString()
}

fun ByteArray.toHexString(): String {
  return this.toHexString(0, this.size, true)
}

fun Long.toHexByteArray(): ByteArray {
  return this.toString(16).hexStringToByteArray()
}

fun Int.toHexByteArray(): ByteArray {
  return this.toString(16).hexStringToByteArray()
}

fun BigInteger.toHexByteArray(): ByteArray {
  return this.toString(16).hexStringToByteArray()
}
