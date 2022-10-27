package com.easy.defi.app.feature.earn.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.easy.defi.app.feature.earn.EarnListScreen

const val earnNavigationRoute = "earn_route"

fun NavController.navigateToEarn(navOptions: NavOptions? = null) {
  this.navigate(earnNavigationRoute, navOptions)
}

fun NavGraphBuilder.earnGraph() {
  composable(route = earnNavigationRoute) {
    EarnListScreen()
  }
}
