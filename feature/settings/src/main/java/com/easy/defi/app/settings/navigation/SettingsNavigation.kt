package com.easy.defi.app.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.app.settings.SettingsScreen

const val settingsEntryRoute = "settings_route"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
  this.navigate(settingsEntryRoute, navOptions)
}

fun NavGraphBuilder.settingsGraph(
  onBackClick: () -> Unit,
) {
  composableWithAnimation(route = settingsEntryRoute) {
    SettingsScreen(onBackClick = onBackClick)
  }
}
