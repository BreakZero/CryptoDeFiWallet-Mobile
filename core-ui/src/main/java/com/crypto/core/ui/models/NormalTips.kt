package com.crypto.core.ui.models

import androidx.annotation.DrawableRes

data class NormalTips(
  val title: String,
  val message: String,
  @DrawableRes val iconRes: Int,
)
