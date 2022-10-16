package com.crypto.defi.feature.assets

import androidx.annotation.DrawableRes
import com.crypto.core.common.UiText
import com.crypto.defi.models.domain.Asset

data class PromoCard(
  @DrawableRes val backgroundRes: Int,
  val title: UiText
)

data class MainAssetState(
  val assets: List<Asset> = emptyList(),
  val promoCard: List<PromoCard> = emptyList(),
  val totalBalance: String = "0.0",
  val onRefreshing: Boolean = true
)
