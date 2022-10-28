package com.easy.defi.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenHoldingDto(
  @SerialName("amount")
  val amount: String,
  @SerialName("contract_address")
  val contractAddress: String,
  @SerialName("contract_name")
  val contractName: String,
)
