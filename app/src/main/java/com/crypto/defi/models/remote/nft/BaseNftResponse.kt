package com.crypto.defi.models.remote.nft

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class BaseNftResponse<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("msg")
    val message: String? = null,
    @SerialName("data")
    val `data`: T
)
