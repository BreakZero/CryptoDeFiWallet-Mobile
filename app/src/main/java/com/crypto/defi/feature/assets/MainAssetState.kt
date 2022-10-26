package com.crypto.defi.feature.assets

import androidx.annotation.DrawableRes
import com.crypto.core.common.UiText
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.domain.WalletNameInfo

data class PromoCard(
  @DrawableRes val backgroundRes: Int,
  val title: UiText,
)

data class MainAssetState(
  val walletNameInfo: WalletNameInfo = WalletNameInfo.Default,
  val assets: List<Asset> = emptyList(),
  val promoCard: List<PromoCard> = emptyList(),
  val totalBalance: String = "0.0",
  val onRefreshing: Boolean = true,
)
