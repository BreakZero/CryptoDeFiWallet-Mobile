package com.easy.defi.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.easy.defi.app.feature.dapp.navigation.dappGraph
import com.easy.defi.app.feature.earn.navigation.earnGraph
import com.easy.defi.app.feature.nft.navigation.nftGraph
import com.easy.defi.app.onboarding.onBoardingGraph
import com.easy.defi.app.settings.navigation.navigateToSettings
import com.easy.defi.app.settings.navigation.settingsGraph
import com.easy.defi.feature.asset.navigation.assetGraphRoutePattern
import com.easy.defi.feature.asset.navigation.navigateToWallet
import com.easy.defi.feature.asset.navigation.walletGraph
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeFiNavHost(
  navController: NavHostController,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  startDestination: String = assetGraphRoutePattern,
) {
  AnimatedNavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier,
  ) {
    onBoardingGraph(
      navController,
      navigateToMain = {
        navController.navigateToWallet(
          navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
              inclusive = true
              saveState = true
            }
            launchSingleTop = true
            restoreState = true
          },
        )
      },
    )
    walletGraph(
      navigateToSettings = {
        navController.navigateToSettings()
      },
      nestedGraphs = {
        settingsGraph(onBackClick)
      },
    )
    dappGraph()
    nftGraph()
    earnGraph()
  }
}
