package com.easy.defi.app.core.model.data

import java.math.BigInteger

data class ReadyToSign(
  val to: String,
  val amount: BigInteger,
  val memo: String? = null,
  val contract: String? = null,
  val chainId: Int = 3,
)
