package com.crypto.defi.chains.evm

import android.util.Log
import com.crypto.core.extensions.clearHexPrefix
import com.crypto.defi.chains.IChain
import com.crypto.defi.models.remote.BaseRpcResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.math.BigInteger

class EvmChainImpl(
    private val httpClient: HttpClient
): IChain {
    override fun address(): String {
        return "0x81080a7e991bcDdDBA8C2302A70f45d6Bd369Ab5"
    }
    override suspend fun balance(contract: String?): BigInteger {
        return try {
            val response = httpClient.get("http://192.168.1.105:8080/ethereum/balance/${address()}}") {
                contract?.also {
                    parameter("contract", it)
                }
            }.body<BaseRpcResponse<String>>()
            response.result.clearHexPrefix().toBigInteger(16)
        } catch (e: Exception) {
            Log.d("=====", "${e.message}")
            BigInteger.ZERO
        }
    }

    override suspend fun transactions(contract: String?): List<String> {
        return emptyList()
    }
}