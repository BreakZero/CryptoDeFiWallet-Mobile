package com.crypto.defi.models.domain

@kotlinx.serialization.Serializable
data class Asset(
    val slug: String,
    val code: String,
    val iconUrl: String,
    val name: String,
    val symbol: String,
    val decimal: Int,
    val chainName: String,
    val contract: String? = null,
    val nativeBalance: String = "0.0",
    val fiatBalance: String = "0.0"
)
