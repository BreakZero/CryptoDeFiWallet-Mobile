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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.easy.defi.app.feature.dapp.launch.navigation.dAppLaunchScreen
import com.easy.defi.app.feature.dapp.launch.navigation.navigateIntoDApp
import com.easy.defi.app.feature.dapp.navigation.dAppGraph
import com.easy.defi.app.feature.earn.navigation.earnGraph
import com.easy.defi.app.feature.nft.detail.navigation.navigateToNftDetail
import com.easy.defi.app.feature.nft.detail.navigation.nftDetailScreen
import com.easy.defi.app.feature.nft.navigation.nftGraph
import com.easy.defi.app.onboarding.OnBoardingNavigations
import com.easy.defi.app.onboarding.onBoardingGraph
import com.easy.defi.app.settings.navigation.navigateToSettings
import com.easy.defi.app.settings.navigation.settingsScreen
import com.easy.defi.feature.asset.list.navigation.assetGraphRoutePattern
import com.easy.defi.feature.asset.list.navigation.walletGraph
import com.easy.defi.feature.asset.send.navigation.navigateToSend
import com.easy.defi.feature.asset.send.navigation.transactionSendScreen
import com.easy.defi.feature.asset.transactions.navigation.toTransactionList
import com.easy.defi.feature.asset.transactions.navigation.transactionListScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeFiOnBoardingNavHost(
  navController: NavHostController,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  startDestination: String = OnBoardingNavigations.Index.destination
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
  startDestination: String = assetGraphRoutePattern
) {
  AnimatedNavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {
    settingsScreen(onBackClick)

    walletGraph(
      navigateToSettings = {
        navController.navigateToSettings()
      },
      navigateToTransactionList = {
        navController.toTransactionList(it)
      },
      nestedGraphs = {
        transactionListScreen(onBackClick = onBackClick, navigateToSend = {
          navController.navigateToSend(it)
        })
        transactionSendScreen(onBackClick)
      }
    )
    dAppGraph(
      navigateToSettings = {
        navController.navigateToSettings()
      },
      navigateIntoDApp = { chainId, url, rpc ->
        navController.navigateIntoDApp(chainId, url, rpc)
      }
    )
    nftGraph(
      navigateToSettings = {
        navController.navigateToSettings()
      },
      navigateToNftDetail = { contractAddress, tokenId ->
        navController.navigateToNftDetail(contractAddress, tokenId)
      }
    )
    earnGraph()

    dAppLaunchScreen(onBackClick)
    nftDetailScreen(onBackClick)
  }
}
