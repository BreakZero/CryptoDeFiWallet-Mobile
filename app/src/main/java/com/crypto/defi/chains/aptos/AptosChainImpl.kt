package com.crypto.defi.chains.aptos

import com.crypto.defi.chains.IChain
import com.crypto.defi.feature.assets.send.ReadyToSign
import com.crypto.defi.feature.assets.send.TransactionPlan
import com.crypto.defi.models.domain.BaseTransaction
import io.ktor.client.*
import timber.log.Timber
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.math.BigInteger

class AptosChainImpl(
  private val httpClient: HttpClient,
  private val hdWallet: HDWallet
) : IChain {
  override fun address(): String {
    return try {
      hdWallet.getAddressForCoin(CoinType.APTOS)
    } catch (e: Exception) {
      e.printStackTrace()
      ""
    }
  }

  override suspend fun balance(contract: String?): BigInteger {
    return "100000000".toBigInteger()
  }

  override suspend fun transactions(page: Int, offset: Int, contract: String?): List<BaseTransaction> {
    return emptyList()
  }

  override suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan {
    TODO("Not yet implemented")
  }

  override suspend fun broadcast(rawData: String): String {
    return ""
  }
}