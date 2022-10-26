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

  val Settings_Currency = object : NavigationCommand {
    override val arguments: List<NamedNavArgument>
      get() = emptyList()
    override val destination: String
      get() = "settings-currencies"
  }

  val Settings_Chain = object : NavigationCommand {
    override val arguments: List<NamedNavArgument>
      get() = emptyList()
    override val destination: String
      get() = "settings-multi-chain"
  }
}
