package com.crypto.defi.feature.assets.send

data class SendFormState(
    val to: String,
    val fee: String,
    val amount: String,
    val memo: String
)
