package com.easy.defi.app.core.ui.navigation

import androidx.navigation.NamedNavArgument

interface NavigationCommand {
  val arguments: List<NamedNavArgument>
  val destination: String
}
