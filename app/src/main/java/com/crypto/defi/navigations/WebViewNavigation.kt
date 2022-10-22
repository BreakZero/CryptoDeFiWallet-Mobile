package com.crypto.defi.navigations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object WebViewNavigation {
  const val KEY_WEBSITE_URL = "website_url"
  const val WEBSITE_DESTINATION = "website-with-url?${KEY_WEBSITE_URL}={${KEY_WEBSITE_URL}}"

  val args = listOf(
    navArgument(KEY_WEBSITE_URL) { type = NavType.StringType }
  )

  fun visitWebsite(
    url: String
  ) = object : NavigationCommand {
    override val arguments
      get() = args
    override val destination = "website-with-url?${KEY_WEBSITE_URL}=$url"
  }
}