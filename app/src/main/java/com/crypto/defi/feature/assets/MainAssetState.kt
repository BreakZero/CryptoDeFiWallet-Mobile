package com.crypto.defi.feature.assets

import androidx.annotation.DrawableRes
import com.crypto.core.common.UiText
import com.crypto.core.model.NetworkStatus
import com.crypto.defi.models.domain.Asset

data class PromoCart(
    @DrawableRes val backgroundRes: Int,
    val title: UiText
)

data class MainAssetState(
    val assets: List<Asset> = emptyList(),
    val promoCard: List<PromoCart> = emptyList(),
    val totalBalance: String = "0.0",
    val onRefreshing: Boolean = true
)
