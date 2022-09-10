package com.crypto.defi.chains.solana

import com.crypto.defi.chains.IChain
import java.math.BigInteger

class SolanaChainImpl: IChain {
    override fun address(): String {
        return ""
    }
    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ZERO
    }

    override suspend fun transactions(ccontract: String?): List<String> {
        return emptyList()
    }
}