package com.crypto.defi.chains

import java.math.BigInteger

class EmptyChain: IChain {
    override fun address(): String {
        return ""
    }
    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ZERO
    }

    override suspend fun transactions(contract: String?): List<String> {
        return emptyList()
    }
}