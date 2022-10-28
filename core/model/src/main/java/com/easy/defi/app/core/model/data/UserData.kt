package com.easy.defi.app.core.model.data

enum class ChainNetwork(
  val label: String,
) {
  MAINNET("MainNet"), ROPSTEN("Ropsten"), RINKEBY("Rinkeby");

  companion object {
    fun fromLabel(label: String): ChainNetwork {
      return ChainNetwork.MAINNET
    }
  }
}

data class DeFiCurrency(
  val code: String,
  val symbol: String,
)

data class UserData(
  val passcode: String,
  val currency: DeFiCurrency,
  val network: ChainNetwork,
  val walletProfile: WalletProfile,
)
