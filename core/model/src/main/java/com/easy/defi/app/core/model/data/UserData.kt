package com.easy.defi.app.core.model.data

enum class ChainNetwork(
  val label: String,
) {
  MAINNET("MainNet"), ROPSTEN("Ropsten"), RINKEBY("Rinkeby")
}

data class DeFiCurrency(
  val code: String,
  val symbol: String,
)

data class UserData(
  val currency: DeFiCurrency,
  val network: ChainNetwork,
  val walletProfile: WalletProfile,
)
