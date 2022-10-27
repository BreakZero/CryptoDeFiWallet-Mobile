package com.easy.defi.app.core.domain.chains

import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.model.data.ReadyToSign
import com.easy.defi.app.core.model.data.TransactionPlan
import java.math.BigInteger

interface IChain {
  fun address(): String
  suspend fun balance(contract: String? = null): BigInteger
  suspend fun transactions(page: Int, offset: Int, contract: String? = null): List<BaseTransaction>

  suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan

  suspend fun broadcast(rawData: String): String
}
