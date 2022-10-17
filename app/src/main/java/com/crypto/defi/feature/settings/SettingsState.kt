package com.crypto.defi.feature.settings

import com.crypto.defi.common.DeFiConstant
import com.crypto.defi.models.domain.ChainNetwork
import com.crypto.defi.models.domain.DeFiCurrency
import com.crypto.defi.models.domain.WalletNameInfo
import java.util.*

data class SettingsState(
  val currency: DeFiCurrency = Currency.getInstance(Locale.US).let {
    DeFiCurrency(it.symbol, it.currencyCode)
  },
  val network: ChainNetwork = ChainNetwork.MAINNET,
  val walletNameInfo: WalletNameInfo = WalletNameInfo.Default
)
