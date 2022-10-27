package com.easy.defi.app.core.domain.chains

import com.easy.defi.app.core.model.data.EvmTransaction
import com.easy.defi.app.core.model.data.ReadyToSign
import com.easy.defi.app.core.model.data.TransactionPlan
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
