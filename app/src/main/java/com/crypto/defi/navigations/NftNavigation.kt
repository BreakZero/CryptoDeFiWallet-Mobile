package com.crypto.defi.navigations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object NftNavigation {
  val groupDestination = object : NavigationCommand {
    override val arguments: List<NamedNavArgument>
      get() = emptyList()
    override val destination: String
      get() = "nft-group"
  }

  const val NFT_TOKEN_ID = "nft_token_id"
  const val NFT_DETAIL_ROUTE = "nft_detail?${NFT_TOKEN_ID}={${NFT_TOKEN_ID}}"
  val args = listOf(
    navArgument(NFT_TOKEN_ID) { type = NavType.StringType }
  )

  fun detailDestination(
    token_id: String
  ) = object : NavigationCommand {
    override val arguments
      get() = TransactionListNavigation.args
    override val destination = "nft_detail?${NFT_TOKEN_ID}=$token_id"
  }
}