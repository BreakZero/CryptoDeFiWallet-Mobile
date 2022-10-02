package com.crypto.defi.chains.usecase

import com.crypto.core.extensions.orElse
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.entities.AssetEntity
import com.crypto.defi.models.local.entities.CoinVersionShaEntity
import com.crypto.defi.models.local.entities.TierEntity
import com.crypto.defi.models.mapper.toAsset
import com.crypto.defi.models.mapper.toAssetEntity
import com.crypto.defi.models.remote.AssetDto
import com.crypto.defi.models.remote.BaseResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class AssetUseCase @Inject constructor(
    private val client: HttpClient,
    private val database: CryptoDeFiDatabase
) {
    suspend fun fetchingAssets(
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

    fun assetsFlow(): Flow<List<AssetEntity>> {
        return try {
            database.assetDao.assetsFlow().map {
                it.filter { it.chainName == "Ethereum" }
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

    fun findAssetBySlug(slug: String): Flow<Asset?> {
        val assetEntity = flow { emit(database.assetDao.assetBySlug(slug)) }
        val rateEntity = database.tierDao.findBySlug(slug)
        return combine(assetEntity, rateEntity) { asset, rate ->
            asset?.toAsset()?.copy(rate = rate.rate.toBigDecimalOrNull() ?: BigDecimal.ZERO)
        }
    }
}