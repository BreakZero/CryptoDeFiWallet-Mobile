package com.crypto.core.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetStatusColor(
  statusColor: Color,
) {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = !isSystemInDarkTheme()
  LaunchedEffect(key1 = null) {
    systemUiController.setStatusBarColor(
      color = statusColor,
      darkIcons = useDarkIcons,
    )
  }
}
