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

package com.easy.defi.navigation

import com.easy.defi.R
import com.easy.defi.app.core.designsystem.icon.DeFiIcons
import com.easy.defi.app.core.designsystem.icon.Icon
import com.easy.defi.app.core.designsystem.icon.Icon.DrawableResourceIcon

enum class TopLevelDestination(
  val selectedIcon: Icon,
  val unselectedIcon: Icon,
  val iconTextId: Int,
  val titleTextId: Int
) {
  WALLET(
    selectedIcon = DrawableResourceIcon(DeFiIcons.Wallet),
    unselectedIcon = DrawableResourceIcon(DeFiIcons.Wallet),
    iconTextId = R.string.wallet,
    titleTextId = R.string.app_name
  ),
  NFTS(
    selectedIcon = DrawableResourceIcon(DeFiIcons.Nfts),
    unselectedIcon = DrawableResourceIcon(DeFiIcons.Nfts),
    iconTextId = R.string.nft,
    titleTextId = R.string.nft
  ),
  DAPPS(
    selectedIcon = DrawableResourceIcon(DeFiIcons.DApps),
    unselectedIcon = DrawableResourceIcon(DeFiIcons.DApps),
    iconTextId = R.string.dapps,
    titleTextId = R.string.dapps
  ),
  EARN(
    selectedIcon = DrawableResourceIcon(DeFiIcons.Earn),
    unselectedIcon = DrawableResourceIcon(DeFiIcons.Earn),
    iconTextId = R.string.earn,
    titleTextId = R.string.earn
  )
}
