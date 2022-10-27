package com.easy.defi.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TierDto(
  @SerialName("from_currency")
  val fromCurrency: String,
  @SerialName("from_slug")
  val fromSlug: String,
  @SerialName("rates")
  val rates: List<RateDto>,
  @SerialName("time_stamp")
  val timeStamp: Int,
  @SerialName("to_currency")
  val toCurrency: String,
)

@Serializable
data class RateDto(
  @SerialName("amount")
  val amount: String,
  @SerialName("rate")
  val rate: String,
)
