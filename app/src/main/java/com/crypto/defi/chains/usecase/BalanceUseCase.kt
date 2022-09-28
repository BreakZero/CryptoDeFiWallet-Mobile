package com.crypto.defi.chains.usecase

import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.entities.TierEntity
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.TierDto
import com.crypto.defi.models.remote.TokenHolding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import timber.log.Timber
import javax.inject.Inject

class BalanceUseCase @Inject constructor(
    private val client: HttpClient,
    private val database: CryptoDeFiDatabase
) {
    suspend fun tokenHolding(
        chain: String = "ethereum",
        address: String
    ): List<TokenHolding> {
        return try {
            val result = client.get("${UrlConstant.BASE_URL}/$chain/$address/tokenholdings")
                .body<BaseResponse<List<TokenHolding>>>()
            result.data.onEach {
                database.assetDao.updateBalance(contract = it.contractAddress, balance = it.amount)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchingTiers() {
        try {
            val result = client.get("${UrlConstant.BASE_URL}/tiers").body<BaseResponse<List<TierDto>>>()
            database.tierDao.insertAll(result.data.map { tier ->
                TierEntity(
                    timeStamp = tier.timeStamp.toString(),
                    fromCurrency = tier.fromCurrency,
                    toCurrency = tier.toCurrency,
                    rate = tier.rates.minByOrNull { it.amount }?.rate
                        ?: "0.00",
                    fromSlug = tier.fromSlug
                )
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun mainCoinBalance(
        chain: String = "",
        address: String
    ) {

    }
}