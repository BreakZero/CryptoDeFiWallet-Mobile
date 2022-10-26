package com.crypto.defi.feature.dapps.detail

import androidx.annotation.Keep
import com.crypto.defi.feature.dapps.detail.utils.DAppMethod

@Keep
data class DAppState(
  val title: String = "",
  val method: DAppMethod = DAppMethod.UNKNOWN,
  val methodId: Long = -1L,
  val data: String = "",
)
