package com.easy.defi.app.settings

import com.easy.defi.app.core.model.data.ChainNetwork
import com.easy.defi.app.core.model.data.DeFiCurrency
import com.easy.defi.app.core.model.data.WalletProfile
import java.util.*

data class SettingsState(
  val currency: DeFiCurrency = Currency.getInstance(Locale.US).let {
    DeFiCurrency(it.symbol, it.currencyCode)
  },
  val network: ChainNetwork = ChainNetwork.MAINNET,
  val walletProfile: WalletProfile = WalletProfile(null, ""),
)
