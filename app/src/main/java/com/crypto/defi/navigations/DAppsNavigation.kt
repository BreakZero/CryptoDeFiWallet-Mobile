package com.crypto.defi.navigations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object DAppsNavigation {
  const val KEY_OF_DAPP_CHAIN_ID = "key_dapp_chain_id"
  const val KEY_OF_DAPP_URL = "key_dapp_url"
  const val KEY_OF_DAPP_RPC = "key_dapp_rpc"
  const val DAPP_DETAIL_ROUTE = "dapp_detail?${KEY_OF_DAPP_CHAIN_ID}={${KEY_OF_DAPP_CHAIN_ID}}&${KEY_OF_DAPP_URL}={${KEY_OF_DAPP_URL}}&${KEY_OF_DAPP_RPC}={${KEY_OF_DAPP_RPC}}"
  val detailsArgs = listOf(
    navArgument(KEY_OF_DAPP_CHAIN_ID) { type = NavType.IntType },
    navArgument(KEY_OF_DAPP_URL) { type = NavType.StringType },
    navArgument(KEY_OF_DAPP_RPC) { type = NavType.StringType }
  )

  fun detailDestination(
    chainId: Int,
    dAppUrl: String,
    dAppRpc: String
  ) = object : NavigationCommand {
    override val arguments
      get() = detailsArgs
    override val destination = "dapp_detail?${KEY_OF_DAPP_CHAIN_ID}=$chainId&${KEY_OF_DAPP_URL}=$dAppUrl&${KEY_OF_DAPP_RPC}=$dAppRpc"
  }
}