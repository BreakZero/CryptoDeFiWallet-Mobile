package com.crypto.defi.feature.dapps.detail.utils

import androidx.annotation.Keep

@Keep
data class MessageInfo(
  val title: String,
  val methodId: Long,
  val method: DAppMethod,
  val from: String ? = null,
  val to: String? = null,
  val data: String
) {
  fun isEmpty(): Boolean {
    return this == Empty
  }
  companion object {
    val Empty = MessageInfo(
      title = "",
      methodId = -1L,
      method = DAppMethod.UNKNOWN,
      data = ""
    )
  }
}