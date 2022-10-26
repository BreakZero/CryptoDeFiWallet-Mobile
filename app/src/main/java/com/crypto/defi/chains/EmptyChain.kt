package com.crypto.defi.chains

import com.crypto.defi.feature.assets.send.ReadyToSign
import com.crypto.defi.feature.assets.send.TransactionPlan
import com.crypto.defi.models.domain.EvmTransaction
import java.math.BigInteger

class EmptyChain : IChain {
  override fun address(): String {
    return ""
  }

  override suspend fun balance(contract: String?): BigInteger {
    return BigInteger.ZERO
  }

  override suspend fun transactions(
    page: Int,
    offset: Int,
    contract: String?,
  ): List<EvmTransaction> {
    return emptyList()
  }

  override suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan {
    return TransactionPlan.EmptyPlan
  }

  override suspend fun broadcast(rawData: String): String {
    return ""
  }
}
