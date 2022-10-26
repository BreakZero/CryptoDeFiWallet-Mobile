package com.crypto.defi.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenHolding(
  @SerialName("amount")
  val amount: String,
  @SerialName("contract_address")
  val contractAddress: String,
  @SerialName("contract_name")
  val contractName: String,
)
