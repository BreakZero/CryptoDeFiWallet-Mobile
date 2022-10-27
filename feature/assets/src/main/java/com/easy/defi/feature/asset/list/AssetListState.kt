package com.easy.defi.feature.asset.list

import androidx.annotation.DrawableRes
import com.easy.defi.app.core.common.utils.DeFiConstant
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.data.WalletProfile
import com.easy.defi.app.core.ui.UiText

data class PromoCard(
  @DrawableRes val backgroundRes: Int,
  val title: UiText,
)

data class AssetListState(
  val walletProfile: WalletProfile = WalletProfile(null, DeFiConstant.DEFAULT_WALLET_NAME),
  val assets: List<Asset> = emptyList(),
  val promoCard: List<PromoCard> = emptyList(),
  val totalBalance: String = "0.0",
  val onRefreshing: Boolean = true,
)
