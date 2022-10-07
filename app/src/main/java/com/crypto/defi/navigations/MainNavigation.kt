package com.crypto.defi.navigations

import androidx.navigation.NamedNavArgument
import com.crypto.core.ui.routers.NavigationCommand

object MainNavigation {
  val Main = object : NavigationCommand {
    override val arguments: List<NamedNavArgument>
      get() = emptyList()
    override val destination: String
      get() = "main-home"

  }
}