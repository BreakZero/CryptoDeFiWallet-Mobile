package com.crypto.defi.models.remote

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class BaseResponse<T>(
  @SerialName("code")
  val code: Int,
  @SerialName("error")
  val error: String? = null,
  @SerialName("data")
  val `data`: T,
)
