package com.easy.defi.app.core.model.data

data class Chain(
  val code: String,
  val chainType: String,
  val chainId: String? = null,
  val isTestNet: Boolean,
  val name: String,
  val isToken: Boolean,
)
