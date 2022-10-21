package com.crypto.defi.feature.settings.multi_chain

import com.crypto.defi.models.domain.ChainNetwork
import com.crypto.defi.models.domain.DeFiCurrency
import java.util.*

data class MultiChainState(
  val selected: ChainNetwork = ChainNetwork.MAINNET,
  val supports: List<ChainNetwork> = emptyList()
)
