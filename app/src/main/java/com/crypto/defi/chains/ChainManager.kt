package com.crypto.defi.chains

import com.crypto.defi.chains.evm.EvmChainImpl
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.ChainDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import javax.inject.Inject

class ChainManager @Inject constructor(
    private val database: CryptoDeFiDatabase,
    private val client: HttpClient
) {
  private lateinit var hdWallet: HDWallet

  fun evmAddress(): String {
    return hdWallet.getAddressForCoin(CoinType.ETHEREUM)
  }

  private val chain = hashMapOf<String, IChain>()
  private val lock = Mutex()

  suspend fun fetchingChains(hdWallet: HDWallet) {
    this.hdWallet = hdWallet
    val chains = try {
      client.get("${UrlConstant.BASE_URL}/chains")
          .body<BaseResponse<List<ChainDto>>>().data.map {
            ChainEntity(
                code = it.code,
                chainType = it.chainTypes?.let {
                  "evm"
                } ?: it.parentChain,
                chainId = it.details?.chainId,
                isTestNet = it.isTestnet,
                name = it.name
            )
          }.also {
            withContext(Dispatchers.Default) {
              database.chainDao.insertAll(it)
            }
          }
    } catch (e: Exception) {
      e.printStackTrace()
      database.chainDao.chains()
    }
    lock.withLock {
      chains.onEach {
        chain[it.code] = when (it.chainType) {
          "evm" -> EvmChainImpl(client, hdWallet)
          else -> EmptyChain()
        }
      }
    }
  }

  suspend fun getChainByKey(code: String): IChain {
    return lock.withLock {
      chain[code] ?: EmptyChain()
    }
  }
}