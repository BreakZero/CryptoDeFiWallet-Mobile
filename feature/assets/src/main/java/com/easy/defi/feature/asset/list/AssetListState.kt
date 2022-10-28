/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
