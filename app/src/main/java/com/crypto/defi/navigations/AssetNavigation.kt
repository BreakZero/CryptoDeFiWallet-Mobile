package com.crypto.defi.navigations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object TransactionListNavigation {
  const val KEY_CODE = "asset-slug"
  const val ROUTE = "transaction_list?${KEY_CODE}={${KEY_CODE}}"
  val args = listOf(
      navArgument(KEY_CODE) { type = NavType.StringType }
  )

  fun destination(
      slug: String
  ) = object : NavigationCommand {
    override val arguments
      get() = args
    override val destination = "transaction_list?${KEY_CODE}=$slug"
  }
}

object SendFormNavigation {
  const val KEY_SLUG = "asset-slug"
  const val ROUTE = "sending_form?${KEY_SLUG}={${KEY_SLUG}}"
  val args = listOf(
      navArgument(TransactionListNavigation.KEY_CODE) { type = NavType.StringType }
  )

  fun destination(
      slug: String
  ) = object : NavigationCommand {
    override val arguments
      get() = args
    override val destination = "sending_form?${KEY_SLUG}=$slug"
  }
}