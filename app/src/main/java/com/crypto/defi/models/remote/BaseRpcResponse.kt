package com.crypto.defi.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseRpcResponse<T>(
    @SerialName("jsonrpc")
    val jsonrpc: String,
    @SerialName("id")
    val id: Int,
    @SerialName("result")
    val result: T
)
