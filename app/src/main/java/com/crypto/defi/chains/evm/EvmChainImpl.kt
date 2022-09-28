package com.crypto.defi.chains.evm

import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.chains.IChain
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.remote.BaseRpcResponse
import com.crypto.wallet.WalletRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.math.BigInteger

private val json = Json {
    ignoreUnknownKeys = true
}

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
            }.bodyAsText()
            val resule = json.decodeFromString(BaseRpcResponse.serializer(String.serializer()), response).result
            resule.clearHexPrefix().toBigInteger(16)
        } catch (e: Exception) {
            BigInteger.ZERO
        }
    }

    override suspend fun transactions(contract: String?): List<String> {
        return emptyList()
    }
}