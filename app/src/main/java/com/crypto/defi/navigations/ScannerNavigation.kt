package com.crypto.defi.navigations

import androidx.navigation.NamedNavArgument
import com.crypto.core.ui.routers.NavigationCommand

object ScannerNavigation {
  val Scanner = object : NavigationCommand {
    override val arguments: List<NamedNavArgument>
      get() = emptyList()
    override val destination: String
      get() = "scan-qr-code"
  }
}
