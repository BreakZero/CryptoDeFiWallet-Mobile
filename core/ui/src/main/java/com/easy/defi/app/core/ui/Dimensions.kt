package com.easy.defi.app.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
  val default: Dp = 0.dp,
  val extraSmall: Dp = 4.dp,
  val small: Dp = 8.dp,
  val medium: Dp = 16.dp,
  val large: Dp = 32.dp,
  val extraLarge: Dp = 64.dp,

  val space12: Dp = 12.dp,
  val space24: Dp = 24.dp,
  val space48: Dp = 48.dp,
  val space56: Dp = 56.dp,
  val space128: Dp = 128.dp,
)

val LocalSpacing = compositionLocalOf { Dimensions() }

val MaterialTheme.Spacing: Dimensions
  @Composable
  @ReadOnlyComposable
  get() = LocalSpacing.current