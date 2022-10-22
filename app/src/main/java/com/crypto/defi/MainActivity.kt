package com.crypto.defi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.crypto.core.ui.composables.NormalTipsView
import com.crypto.core.ui.models.NormalTips
import com.crypto.core.ui.utils.SetStatusColor
import com.crypto.defi.common.MapKeyConstants
import com.crypto.defi.feature.assets.send.SendFormScreen
import com.crypto.defi.feature.assets.send.sendFormViewModel
import com.crypto.defi.feature.assets.transactions.TransactionListScreen
import com.crypto.defi.feature.assets.transactions.transactionListViewModel
import com.crypto.defi.feature.common.DeFiScannerScreen
import com.crypto.defi.feature.common.DeFiWebViewScreen
import com.crypto.defi.feature.dapps.detail.DAppDetailScreen
import com.crypto.defi.feature.main.MainScreen
import com.crypto.defi.feature.nfts.detail.NftDetailScreen
import com.crypto.defi.feature.nfts.group.NftGroupScreen
import com.crypto.defi.feature.settings.SettingsScreen
import com.crypto.defi.feature.settings.currencies.SettingsCurrencyScreen
import com.crypto.defi.feature.settings.multi_chain.SettingsMultiChainScreen
import com.crypto.defi.feature.splash.SplashScreen
import com.crypto.defi.navigations.*
import com.crypto.defi.ui.theme.DeFiWalletTheme
import com.crypto.onboarding.presentation.onboarding
import com.crypto.resource.R
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableWithAnimation(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
  composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = {
      fadeIn(animationSpec = tween(500))
    },
    exitTransition = {
      fadeOut(animationSpec = tween(500))
    },
    popEnterTransition = {
      fadeIn(animationSpec = tween(500))
    },
    popExitTransition = {
      fadeOut(animationSpec = tween(500))
    },
    content = content
  )
}

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      DeFiWalletTheme {
        val navController = rememberAnimatedNavController()
        SetStatusColor(statusColor = MaterialTheme.colorScheme.primary)
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
        ) {
          AnimatedNavHost(
            navController = navController,
            startDestination = SplashNavigation.Splashing.destination,
            modifier = Modifier
          ) {
            composableWithAnimation(
              SplashNavigation.Splashing.destination
            ) {
              SplashScreen {
                navController.navigate(it.destination) {
                  popUpTo(SplashNavigation.Splashing.destination) {
                    inclusive = true
                  }
                }
              }
            }

            composableWithAnimation(
              MainNavigation.Main.destination
            ) {
              MainScreen(
                savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
              ) {
                navController.navigate(it.destination)
              }
            }

            composableWithAnimation(
              route = TransactionListNavigation.ROUTE,
              arguments = TransactionListNavigation.args
            ) { backStackEntry ->
              val coinSlug = backStackEntry.arguments?.getString(TransactionListNavigation.KEY_CODE).orEmpty()
              TransactionListScreen(
                txnListViewModel = transactionListViewModel(slug = coinSlug),
                navigateUp = {
                  navController.popBackStack()
                }
              ) {
                navController.navigate(it.destination)
              }
            }

            composableWithAnimation(
              route = SendFormNavigation.ROUTE,
              arguments = SendFormNavigation.args
            ) { backStackEntry ->
              val coinSlug = backStackEntry.arguments
                ?.getString(SendFormNavigation.KEY_SLUG) ?: ""
              SendFormScreen(
                savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
                sendFormViewModel = sendFormViewModel(coinSlug),
                navigateUp = {
                  navController.popBackStack()
                }) {
                navController.navigate(it.destination)
              }
            }

            composableWithAnimation(
              route = SettingsNavigation.Settings.destination
            ) { _ ->
              SettingsScreen(
                popBack = {
                  navController.popBackStack()
                }
              ) {
                navController.navigate(it.destination)
              }
            }

            composableWithAnimation(
              route = SettingsNavigation.Settings_Currency.destination
            ) { _ ->
              SettingsCurrencyScreen {
                navController.popBackStack()
              }
            }

            composableWithAnimation(
              route = SettingsNavigation.Settings_Chain.destination
            ) { _ ->
              SettingsMultiChainScreen {
                navController.popBackStack()
              }
            }

            composableWithAnimation(
              route = NftNavigation.groupDestination.destination
            ) {
              NftGroupScreen() {
                navController.popBackStack()
              }
            }

            composableWithAnimation(
              route = DAppsNavigation.DAPP_DETAIL_ROUTE,
              arguments = DAppsNavigation.detailsArgs
            ) { backStackEntry ->
              val dAppUrl = backStackEntry.arguments?.getString(DAppsNavigation.KEY_OF_DAPP_URL).orEmpty()
              val dAppRpc = backStackEntry.arguments?.getString(DAppsNavigation.KEY_OF_DAPP_RPC).orEmpty()
              val chainId = backStackEntry.arguments?.getInt(DAppsNavigation.KEY_OF_DAPP_CHAIN_ID, 1) ?: 1
              DAppDetailScreen(
                chainId = chainId,
                dAppUrl = dAppUrl,
                dAppRpc = dAppRpc,
                popBack = {
                  navController.popBackStack()
                }
              )
            }

            composableWithAnimation(
              route = ScannerNavigation.Scanner.destination
            ) { _ ->
              DeFiScannerScreen { content ->
                content?.also { qr_code ->
                  navController.previousBackStackEntry?.savedStateHandle?.let {
                    it[MapKeyConstants.KEY_OF_QR_CODE_CONTENT] = qr_code
                  }
                }
                navController.popBackStack()
              }
            }

            composableWithAnimation(
              route = WebViewNavigation.WEBSITE_DESTINATION,
              arguments = WebViewNavigation.args
            ) {  backStackEntry ->
              val url = backStackEntry.arguments?.getString(WebViewNavigation.KEY_WEBSITE_URL).orEmpty()
              DeFiWebViewScreen(url = url) {
                navController.popBackStack()
              }
            }

            composableWithAnimation(
              route = NftNavigation.NFT_DETAIL_ROUTE,
              arguments = NftNavigation.nftDetailArgs
            ) {
              NftDetailScreen()
            }

            onboarding(navController)
            /**
             * common ui
             */
            dialog("normal_tips") {
              NormalTipsView(
                tips = NormalTips(
                  title = stringResource(id = R.string.two_fa_view__2_factor_authentication),
                  message = stringResource(id = R.string.wallet_protect__2fa_desc),
                  iconRes = R.drawable.ic_shell
                )
              )
            }
          }
        }
      }
    }
  }
}
