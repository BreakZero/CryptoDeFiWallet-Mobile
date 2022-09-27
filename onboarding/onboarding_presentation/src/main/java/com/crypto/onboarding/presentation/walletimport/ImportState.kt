package com.crypto.onboarding.presentation.walletimport

data class ImportState(
    val phrase: String = "",
    val isHintVisible: Boolean = true,
    val inProgress: Boolean = false
)
