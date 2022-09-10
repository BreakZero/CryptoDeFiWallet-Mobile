package com.crypto.defi.chains

import java.math.BigInteger

interface IChain {
    fun address(): String
    suspend fun balance(contract: String? = null): BigInteger
    suspend fun transactions(contract: String? = null): List<String>
}