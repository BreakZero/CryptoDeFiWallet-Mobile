package com.crypto.defi.chains.solana

import com.crypto.defi.chains.IChain
import com.crypto.defi.feature.assets.send.ReadyToSign
import com.crypto.defi.feature.assets.send.TransactionPlan
import com.crypto.defi.models.domain.EvmTransaction
import io.ktor.client.*
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.math.BigInteger

class SolanaChainImpl(
    private val httpClient: HttpClient,
    private val hdWallet: HDWallet
): IChain {
    override fun address(): String {
        return hdWallet.getAddressForCoin(CoinType.SOLANA)
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

    override suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan {
        return TransactionPlan.EmptyPlan
    }
    override suspend fun broadcast(rawData: String): String {
        return ""
    }
}