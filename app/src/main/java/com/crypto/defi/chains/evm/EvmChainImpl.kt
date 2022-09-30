package com.crypto.defi.chains.evm

import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.chains.IChain
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.mapper.toEvmTransaction
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.EvmTransactionDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.math.BigInteger

class EvmChainImpl(
    private val httpClient: HttpClient,
    private val hdWallet: HDWallet
): IChain {
    override fun address(): String {
        return hdWallet.getAddressForCoin(CoinType.ETHEREUM)
    }
    override suspend fun balance(contract: String?): BigInteger {
        return try {
            val response = httpClient.get("${UrlConstant.BASE_URL}/ethereum/balance/${address()}") {
                contract?.also {
                    parameter("contract", it)
                }
            }.body<BaseResponse<String>>()
            response.data.clearHexPrefix().toBigInteger(16)
        } catch (e: Exception) {
            BigInteger.ZERO
        }
    }

    override suspend fun transactions(
        page: Int,
        offset: Int,
        contract: String?
    ): List<EvmTransaction> {
        return try {
            httpClient.get(
                urlString = "${UrlConstant.BASE_URL}/ethereum/transactions/${address()}"
            ) {
                parameter("page", page)
                parameter("offset", 20)
                if (!contract.isNullOrEmpty()) {
                    parameter("contract", contract)
                }
            }.body<BaseResponse<List<EvmTransactionDto>>>().data.map {
                it.toEvmTransaction(address())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}