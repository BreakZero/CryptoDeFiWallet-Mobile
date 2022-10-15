package com.crypto.defi.navigations

import androidx.navigation.NamedNavArgument
import com.crypto.core.ui.routers.NavigationCommand

object SettingsNavigation {
  val Settings = object : NavigationCommand {
    override val arguments: List<NamedNavArgument>
      get() = emptyList()
    override val destination: String
      get() = "settings"

  }
}