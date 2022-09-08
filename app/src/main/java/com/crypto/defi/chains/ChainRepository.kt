package com.crypto.defi.chains

import com.crypto.defi.chains.evm.EvmChainImpl
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.ChainDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChainRepository @Inject constructor(
    val database: CryptoDeFiDatabase,
    val client: HttpClient
) {
    private val chain = hashMapOf<String, IChain>()

    suspend fun fetching() {
        try {
            val chains = client.get("http://192.168.1.105:8080/chains").body<BaseResponse<List<ChainDto>>>().data.map {
                ChainEntity(
                    code = it.code,
                    chainType = it.chainTypes?.let {
                        "evm"
                    } ?: it.parentChain,
                    chainId = it.details?.chainId,
                    isTestNet = it.isTestnet,
                    name = it.name
                )
            }
            withContext(Dispatchers.Default) {
                database.chainDao.insertAll(chains)
            }
            chains.onEach {
                chain[it.code] = when(it.chainType) {
                    "evm" -> EvmChainImpl()
                    else -> EmptyChain()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getChainByKey(code: String): IChain {
        return chain[code] ?: EmptyChain()
    }
}