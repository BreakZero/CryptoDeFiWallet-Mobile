package com.crypto.defi.models.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ChainDto(
  @SerialName("chain_type")
  val chainType: String?,
  @SerialName("code")
  val code: String,
  @SerialName("display_token_name")
  val displayTokenName: String,
  @SerialName("is_testnet")
  val isTestnet: Boolean,
  @SerialName("name")
  val name: String,
  @SerialName("parent_chain")
  val parentChain: String,
  @SerialName("position")
  val position: Int,
  @SerialName("chain_id")
  val chainId: String?,
  @SerialName("is_token")
  val isToken: Boolean
)

@Serializable
internal data class Details(
  @SerialName("chain_id")
  val chainId: String?,
  @SerialName("description")
  val description: String,
  @SerialName("full_description")
  val fullDescription: String,
  @SerialName("icon_link")
  val iconLink: String
)