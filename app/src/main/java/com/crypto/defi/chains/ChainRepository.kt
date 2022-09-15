package com.crypto.defi.chains

import android.util.Log
import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.chains.evm.EvmChainImpl
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.mapper.toAsset
import com.crypto.defi.models.mapper.toAssetEntity
import com.crypto.defi.models.remote.*
import com.crypto.defi.models.remote.AssetDto
import com.crypto.defi.models.remote.ChainDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChainRepository @Inject constructor(
    private val database: CryptoDeFiDatabase,
    private val client: HttpClient
) {
    private val chain = hashMapOf<String, IChain>()

    suspend fun fetchingChain(
        onFinish: suspend () -> Unit = {}
    ) {
        try {
            val chains = client.get("http://192.168.1.109:8080/chains")
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
            }
            withContext(Dispatchers.Default) {
                database.chainDao.insertAll(chains)
            }
            chains.onEach {
                chain[it.code] = when (it.chainType) {
                    "evm" -> EvmChainImpl(client)
                    else -> EmptyChain()
                }
            }
            onFinish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchingAssets() = withContext(Dispatchers.IO) {
        try {
            client.get("http://192.168.1.109:8080/currencies")
                .body<BaseResponse<AssetDto>>()
                .data.currencies.map {
                    it.toAssetEntity()
                }.also {
                    database.assetDao.insertAll(it)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetching(
        initial: suspend () -> Unit
    ) {
        fetchingChain {
            initial()
        }
        // will do not call when currency hash not changed
        fetchingAssets()
    }

    fun assetsFlow(): Flow<List<Asset>> {
        return try {
            database.assetDao.assetsFlow().map {
                it.filter { it.chainName == "Ethereum" }.take(200).map {
                    it.toAsset()
                }
            }
        } catch (e: Exception) {
            flow { emptyList<Asset>() }
        }
    }

    suspend fun localAssets(): List<Asset> {
        return database.assetDao.assets().map {
            it.toAsset()
        }
    }

    suspend fun tokenHoldings(): List<TokenHolding> {
        return try {
            val result = client.get("http://192.168.1.109:8080/chain/0x81080a7e991bcdddba8c2302a70f45d6bd369ab5/tokenholdings")
                .body<BaseResponse<List<TokenHolding>>>()
            result.data
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateBalance(contract: String, balance: String) {
        database.assetDao.updateBalance(contract = contract, balance = balance)
    }

    fun getChainByKey(code: String): IChain {
        return chain[code] ?: EmptyChain()
    }
}