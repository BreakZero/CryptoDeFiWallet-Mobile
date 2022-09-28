package com.crypto.defi.chains

import com.crypto.core.extensions.orElse
import com.crypto.defi.chains.evm.EvmChainImpl
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.entities.AssetEntity
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.local.entities.CoinVersionShaEntity
import com.crypto.defi.models.local.entities.TierEntity
import com.crypto.defi.models.mapper.toAsset
import com.crypto.defi.models.mapper.toAssetEntity
import com.crypto.defi.models.remote.AssetDto
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.ChainDto
import com.crypto.wallet.WalletRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChainRepository @Inject constructor(
    private val database: CryptoDeFiDatabase,
    private val client: HttpClient,
    private val walletRepository: WalletRepository
) {
    private val chain = hashMapOf<String, IChain>()
    private val lock = Mutex()

    private suspend fun fetchingChain() {
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
                    "evm" -> EvmChainImpl(client, walletRepository)
                    else -> EmptyChain()
                }
            }
        }
    }

    private suspend fun fetchingAssets(
        initial: suspend () -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val lastSha256 = database.versionDao.lastVersion()?.sha256.orElse("==")
            val response = client.get("${UrlConstant.BASE_URL}/currencies") {
                parameter("sha256", lastSha256)
            }.body<BaseResponse<AssetDto>>()
            if (response.data.sha256 != lastSha256) {
                database.versionDao.insert(
                    CoinVersionShaEntity(
                        sha256 = response.data.sha256,
                        createAt = System.currentTimeMillis()
                    )
                )
            }
            database.assetDao.insertAll(response.data.currencies.map { it.toAssetEntity() })
        } catch (e: Exception) {
            Timber.e(e)
        }
        initial()
    }

    suspend fun fetching(
        initial: suspend () -> Unit
    ) {
        fetchingChain()
        fetchingAssets {
            initial()
        }
    }

    fun assetsFlow(): Flow<List<AssetEntity>> {
        return try {
            database.assetDao.assetsFlow().map {
                it.filter { it.chainName == "Ethereum" }.take(200)
            }
        } catch (e: Exception) {
            flow { emptyList<Asset>() }
        }
    }

    fun tiersFlow(): Flow<List<TierEntity>> {
        return try {
            database.tierDao.allTiers("USD")
        } catch (e: Exception) {
            flow { emptyList<TierEntity>() }
        }
    }

    suspend fun assetBySlug(slug: String): Asset? {
        return database.assetDao.assetBySlug(slug)?.toAsset()
    }

    suspend fun getChainByKey(code: String): IChain {
        return lock.withLock {
            chain[code] ?: EmptyChain()
        }
    }
}