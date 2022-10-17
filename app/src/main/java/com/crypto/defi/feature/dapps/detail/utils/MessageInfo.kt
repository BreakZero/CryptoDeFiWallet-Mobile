package com.crypto.defi.feature.dapps.detail.utils

data class MessageInfo(
  val title: String,
  val methodId: Long,
  val method: DAppMethod,
  val from: String ? = null,
  val to: String? = null,
  val data: String
)