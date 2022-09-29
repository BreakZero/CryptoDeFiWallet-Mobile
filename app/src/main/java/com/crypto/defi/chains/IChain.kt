package com.crypto.defi.chains

import com.crypto.defi.models.domain.EvmTransaction
import java.math.BigInteger

interface IChain {
    fun address(): String
    suspend fun balance(contract: String? = null): BigInteger
    suspend fun transactions(page: Int, offset:Int, contract: String? = null): List<EvmTransaction>
}