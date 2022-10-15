package com.crypto.defi.chains

import com.crypto.defi.feature.assets.send.ReadyToSign
import com.crypto.defi.feature.assets.send.TransactionPlan
import com.crypto.defi.models.domain.BaseTransaction
import java.math.BigInteger

interface IChain {
  fun address(): String
  suspend fun balance(contract: String? = null): BigInteger
  suspend fun transactions(page: Int, offset: Int, contract: String? = null): List<BaseTransaction>

  suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan

  suspend fun broadcast(rawData: String): String
}