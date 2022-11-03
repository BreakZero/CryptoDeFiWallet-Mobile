package com.easy.defi.app.core.data.repository.chain

import com.easy.defi.app.core.data.di.annotations.Bitcoin
import com.easy.defi.app.core.data.di.annotations.Ethereum
import com.easy.defi.app.core.model.data.Asset
import javax.inject.Inject

class ChainManager @Inject constructor(
  @Ethereum private val ethereumRepository: ChainRepository,
  @Bitcoin private val bitcoinRepository: ChainRepository
) {
  fun getChainByAsset(asset: Asset): ChainRepository? {
    return when (asset.code) {
      in listOf("eth", "eth-erc20") -> ethereumRepository
      "btc-segwit" -> bitcoinRepository
      else -> null
    }
  }
}
