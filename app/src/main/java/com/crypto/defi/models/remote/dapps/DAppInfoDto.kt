package com.crypto.defi.models.remote.dapps

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DAppInfoDto(
  @SerialName("chain_id")
  val chainId: String,
  @SerialName("icon")
  val icon: String,
  @SerialName("name")
  val name: String,
  @SerialName("rpc")
  val rpc: String,
  @SerialName("slug")
  val slug: String,
  @SerialName("url")
  val url: String,
)
