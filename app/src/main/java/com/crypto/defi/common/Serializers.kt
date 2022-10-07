package com.crypto.defi.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal
import java.math.BigInteger

object BigDecimalSerializer : KSerializer<BigDecimal> {
  override fun deserialize(decoder: Decoder): BigDecimal {
    return decoder.decodeString().toBigDecimal()
  }

  override fun serialize(encoder: Encoder, value: BigDecimal) {
    encoder.encodeString(value.toPlainString())
  }

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)
}

object BigIntegerSerializer : KSerializer<BigInteger> {
  override fun deserialize(decoder: Decoder): BigInteger {
    return decoder.decodeString().toBigInteger()
  }

  override fun serialize(encoder: Encoder, value: BigInteger) {
    encoder.encodeString(value.toString())
  }

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("BigInteger", PrimitiveKind.STRING)
}