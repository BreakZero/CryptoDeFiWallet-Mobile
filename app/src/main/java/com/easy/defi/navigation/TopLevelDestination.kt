package com.easy.defi.navigation

import com.easy.defi.R
import com.easy.defi.app.core.designsystem.icon.DeFiIcon
import com.easy.defi.app.core.designsystem.icon.Icon
import com.easy.defi.app.core.designsystem.icon.Icon.DrawableResourceIcon

enum class TopLevelDestination(
  val selectedIcon: Icon,
  val unselectedIcon: Icon,
  val iconTextId: Int,
  val titleTextId: Int,
) {
  WALLET(
    selectedIcon = DrawableResourceIcon(DeFiIcon.Wallet),
    unselectedIcon = DrawableResourceIcon(DeFiIcon.Wallet),
    iconTextId = R.string.wallet,
    titleTextId = R.string.app_name,
  ),
  NFTS(
    selectedIcon = DrawableResourceIcon(DeFiIcon.Nfts),
    unselectedIcon = DrawableResourceIcon(DeFiIcon.Nfts),
    iconTextId = R.string.nft,
    titleTextId = R.string.nft,
  ),
  DAPPS(
    selectedIcon = DrawableResourceIcon(DeFiIcon.DApps),
    unselectedIcon = DrawableResourceIcon(DeFiIcon.DApps),
    iconTextId = R.string.dapps,
    titleTextId = R.string.dapps,
  ),
  EARN(
    selectedIcon = DrawableResourceIcon(DeFiIcon.Earn),
    unselectedIcon = DrawableResourceIcon(DeFiIcon.Earn),
    iconTextId = R.string.earn,
    titleTextId = R.string.earn,
  ),
}
