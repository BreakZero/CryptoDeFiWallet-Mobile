package com.crypto.core.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun setStatusColor(
    statusColor: Color
) {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = !isSystemInDarkTheme()
  SideEffect {
    systemUiController.setStatusBarColor(
        color = statusColor,
        darkIcons = useDarkIcons
    )
  }
  DisposableEffect(systemUiController, useDarkIcons) {
    systemUiController.setSystemBarsColor(
        color = statusColor,
        darkIcons = useDarkIcons
    )
    onDispose { }
  }
}