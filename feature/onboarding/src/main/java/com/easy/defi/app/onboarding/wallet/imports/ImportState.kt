package com.easy.defi.app.onboarding.wallet.imports

data class ImportState(
  val phrase: String = "",
  val isHintVisible: Boolean = true,
  val inProgress: Boolean = false,
)
