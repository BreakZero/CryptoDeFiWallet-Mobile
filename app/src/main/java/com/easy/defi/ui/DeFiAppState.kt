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

package com.easy.defi.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.easy.defi.app.core.data.util.NetworkMonitor
import com.easy.defi.app.feature.dapp.navigation.dappNavigationRoute
import com.easy.defi.app.feature.dapp.navigation.navigateToDApp
import com.easy.defi.app.feature.earn.navigation.earnNavigationRoute
import com.easy.defi.app.feature.earn.navigation.navigateToEarn
import com.easy.defi.app.feature.nft.navigation.navigateToNft
import com.easy.defi.app.feature.nft.navigation.nftNavigationRoute
import com.easy.defi.feature.asset.list.navigation.assetNavigationRoute
import com.easy.defi.feature.asset.list.navigation.navigateToWallet
import com.easy.defi.navigation.TopLevelDestination
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberDeFiAppState(
  windowSizeClass: WindowSizeClass,
  networkMonitor: NetworkMonitor,
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  navController: NavHostController = rememberAnimatedNavController()
): DeFiAppState {
  return remember(navController, coroutineScope, windowSizeClass, networkMonitor) {
    DeFiAppState(navController, coroutineScope, windowSizeClass, networkMonitor)
  }
}

@Stable
class DeFiAppState(
  val navController: NavHostController,
  val coroutineScope: CoroutineScope,
  val windowSizeClass: WindowSizeClass,
  networkMonitor: NetworkMonitor
) {
  val currentDestination: NavDestination?
    @Composable get() = navController
      .currentBackStackEntryAsState().value?.destination

  val currentTopLevelDestination: TopLevelDestination?
    @Composable get() = when (currentDestination?.route) {
      assetNavigationRoute -> TopLevelDestination.WALLET
      nftNavigationRoute -> TopLevelDestination.NFTS
      dappNavigationRoute -> TopLevelDestination.DAPPS
      earnNavigationRoute -> TopLevelDestination.EARN
      else -> null
    }

  val shouldShowBottomBar: Boolean
    get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
      windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

  val shouldShowNavRail: Boolean
    get() = !shouldShowBottomBar

  val isOffline = networkMonitor.isOnline
    .map(Boolean::not)
    .stateIn(
      scope = coroutineScope,
      started = SharingStarted.WhileSubscribed(5_000),
      initialValue = false
    )

  /**
   * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
   * route.
   */
  val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

  /**
   * UI logic for navigating to a top level destination in the app. Top level destinations have
   * only one copy of the destination of the back stack, and save and restore state whenever you
   * navigate to and from it.
   *
   * @param topLevelDestination: The destination the app needs to navigate to.
   */
  fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
    trace("Navigation: ${topLevelDestination.name}") {
      val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
          saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
      }

      when (topLevelDestination) {
        TopLevelDestination.WALLET -> navController.navigateToWallet(topLevelNavOptions)
        TopLevelDestination.NFTS -> navController.navigateToNft(topLevelNavOptions)
        TopLevelDestination.DAPPS -> navController.navigateToDApp(topLevelNavOptions)
        TopLevelDestination.EARN -> navController.navigateToEarn(topLevelNavOptions)
      }
    }
  }

  fun onBackClick() {
    navController.popBackStack()
  }
}
