package com.easy.defi.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.easy.defi.app.feature.dapp.navigation.dappGraph
import com.easy.defi.app.feature.earn.navigation.earnGraph
import com.easy.defi.app.feature.nft.navigation.nftGraph
import com.easy.defi.app.onboarding.OnBoardingNavigations
import com.easy.defi.app.onboarding.onBoardingGraph
import com.easy.defi.app.settings.navigation.navigateToSettings
import com.easy.defi.app.settings.navigation.settingsGraph
import com.easy.defi.feature.asset.navigation.assetGraphRoutePattern
import com.easy.defi.feature.asset.navigation.walletGraph
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeFiOnBoardingNavHost(
  navController: NavHostController,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  startDestination: String = OnBoardingNavigations.Index.destination,
) {
  AnimatedNavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {
    onBoardingGraph(navController)
  }
}

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
    modifier = modifier
  ) {
    walletGraph(
      navigateToSettings = {
        navController.navigateToSettings()
      },
      nestedGraphs = {
        settingsGraph(onBackClick)
      }
    )
    dappGraph()
    nftGraph()
    earnGraph()
  }
}
