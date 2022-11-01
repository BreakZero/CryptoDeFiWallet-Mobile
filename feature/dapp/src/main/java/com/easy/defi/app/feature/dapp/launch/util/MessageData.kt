package com.easy.defi.app.feature.dapp.launch.util

import androidx.annotation.Keep

@Keep
internal data class MessageData(
  val title: String,
  val methodId: Long,
  val method: ActionMethod,
  val from: String? = null,
  val to: String? = null,
  val data: String,
) {
  fun isEmpty(): Boolean {
    return this == Empty
  }

  companion object {
    val Empty = MessageData(
      title = "",
      methodId = -1L,
      method = ActionMethod.UNKNOWN,
      data = "",
    )
  }
}
