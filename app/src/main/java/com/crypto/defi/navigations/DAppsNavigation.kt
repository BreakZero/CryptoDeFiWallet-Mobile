package com.crypto.defi.navigations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object DAppsNavigation {
  const val KEY_OF_DAPP_URL = "key_dapp_url"
  const val KEY_OF_DAPP_RPC = "key_dapp_rpc"
  const val DAPP_DETAIL_ROUTE = "dapp_detail?${KEY_OF_DAPP_URL}={${KEY_OF_DAPP_URL}}&${KEY_OF_DAPP_RPC}=${KEY_OF_DAPP_RPC}"
  val detailsArgs = listOf(
    navArgument(KEY_OF_DAPP_URL) { type = NavType.StringType }
  )

  fun detailDestination(
    dAppUrl: String,
    dAppRpc: String
  ) = object : NavigationCommand {
    override val arguments
      get() = detailsArgs
    override val destination = "dapp_detail?${KEY_OF_DAPP_URL}=$dAppUrl&${KEY_OF_DAPP_RPC}=$dAppRpc"
  }
}