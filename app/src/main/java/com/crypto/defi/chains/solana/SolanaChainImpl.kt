package com.crypto.defi.chains.solana

import com.crypto.defi.chains.IChain
import com.crypto.defi.models.domain.EvmTransaction
import java.math.BigInteger

class SolanaChainImpl: IChain {
    override fun address(): String {
        return ""
    }
    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ZERO
    }

    override suspend fun transactions(
        page: Int,
        offset: Int,
        contract: String?
    ): List<EvmTransaction> {
        return emptyList()
    }
}