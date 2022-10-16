package com.crypto.defi.models.domain

enum class ChainNetwork {
  MainNet, Reposten
}

data class SettingsConfig(
  val currency: String,
  val network: ChainNetwork,
  val walletName: String,
  val avator: String
)
