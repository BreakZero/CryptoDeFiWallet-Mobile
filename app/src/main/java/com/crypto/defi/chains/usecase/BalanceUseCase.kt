package com.crypto.defi.chains.usecase

import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.TokenHolding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
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

    fun mainCoinBalance(
        chain: String = "",
        address: String
    ) {

    }
}