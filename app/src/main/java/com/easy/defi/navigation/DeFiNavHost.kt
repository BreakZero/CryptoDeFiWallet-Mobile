package com.easy.defi.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.easy.defi.app.feature.dapp.navigation.dappGraph
import com.easy.defi.app.feature.earn.navigation.earnGraph
import com.easy.defi.app.feature.nft.navigation.nftGraph
import com.easy.defi.feature.asset.navigation.assetNavigationRoute
import com.easy.defi.feature.asset.navigation.walletGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeFiNavHost(
  navController: NavHostController,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  startDestination: String = assetNavigationRoute,
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier,
  ) {
    walletGraph()
    dappGraph()
    nftGraph()
    earnGraph()
  }
}
