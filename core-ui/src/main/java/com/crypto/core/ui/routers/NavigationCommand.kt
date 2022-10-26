package com.crypto.core.ui.routers

import androidx.navigation.NamedNavArgument

interface NavigationCommand {
  val arguments: List<NamedNavArgument>
  val destination: String
}
