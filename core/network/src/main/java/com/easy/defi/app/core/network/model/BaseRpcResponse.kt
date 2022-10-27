package com.easy.defi.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseRpcResponse<T>(
  @SerialName("id")
  val id: Int,
  @SerialName("jsonrpc")
  val jsonrpc: String,
  @SerialName("result")
  val result: T,
)
