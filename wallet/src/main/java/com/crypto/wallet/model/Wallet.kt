package com.crypto.wallet.model

data class Wallet(
  val mnemonic: String = "",
  val active: Int = 0, // 0 = inactive, 1 = active
  val passphrase: String = "",
)
