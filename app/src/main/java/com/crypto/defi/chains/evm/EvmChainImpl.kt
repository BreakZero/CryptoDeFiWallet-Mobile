package com.crypto.defi.chains.evm

import android.util.Log
import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.chains.IChain
import com.crypto.defi.models.remote.BaseRpcResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.math.BigInteger

private val json = Json {
    ignoreUnknownKeys = true
}

class EvmChainImpl(
    private val httpClient: HttpClient
): IChain {
    override fun address(): String {
        return "0x81080a7e991bcDdDBA8C2302A70f45d6Bd369Ab5"
    }
    override suspend fun balance(contract: String?): BigInteger {
        return try {
            val response = httpClient.get("http://192.168.1.109:8080/ethereum/balance/${address()}") {
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