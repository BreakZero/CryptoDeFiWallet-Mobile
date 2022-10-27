package com.easy.defi.app.core.network

import com.easy.defi.app.core.network.model.AssetDto
import com.easy.defi.app.core.network.model.BaseResponse
import com.easy.defi.app.core.network.model.ChainDto
import com.easy.defi.app.core.network.model.TierDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject
import timber.log.Timber

class AssetDataSource @Inject constructor(
  private val httpClient: HttpClient,
) {
  suspend fun getChains(): List<String> {
    return try {
      httpClient.get("${UrlConstant.BASE_URL}/chains")
        .body<BaseResponse<List<ChainDto>>>().data.map {
          it.chainId.orEmpty()
        }
    } catch (e: Exception) {
      emptyList()
    }
  }

  suspend fun getCurrencies(
    lastSha256: String,
  ): List<String> {
    return try {
      httpClient.get("${UrlConstant.BASE_URL}/currencies") {
        parameter("sha256", lastSha256)
      }.body<BaseResponse<AssetDto>>().data.currencies.map {
        it.chain
      }
    } catch (e: Exception) {
      emptyList()
    }
  }

  suspend fun getTiers(): List<String> {
    return try {
      httpClient.get("${UrlConstant.BASE_URL}/tiers").body<BaseResponse<List<TierDto>>>().data.map {
        it.fromCurrency
      }
    } catch (e: Exception) {
      Timber.e(e)
      emptyList()
    }
  }
}
