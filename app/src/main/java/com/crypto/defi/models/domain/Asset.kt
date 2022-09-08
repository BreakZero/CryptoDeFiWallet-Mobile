package com.crypto.defi.models.domain

data class Asset(
    val slug: String,
    val code: String,
    val iconUrl: String,
    val name: String,
    val symbol: String
)
