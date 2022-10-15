package com.crypto.core.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetStatusColor(
  statusColor: Color
) {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = !isSystemInDarkTheme()
  DisposableEffect(systemUiController, useDarkIcons) {
    systemUiController.setStatusBarColor(
      color = statusColor,
      darkIcons = useDarkIcons
    )
    onDispose { }
  }
}