package com.crypto.defi.feature.dapps

import androidx.annotation.Keep
import com.crypto.defi.models.domain.WalletNameInfo

@Keep
data class DAppInfo(
  val iconUrl: String,
  val appName: String,
  val rpc: String,
  val url: String
)

@Keep
data class MainDAppState(
  val isLoading: Boolean = true,
  val dApps: List<DAppInfo> = emptyList(),
  val walletNameInfo: WalletNameInfo = WalletNameInfo.Default
)
