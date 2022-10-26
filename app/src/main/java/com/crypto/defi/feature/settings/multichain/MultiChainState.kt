package com.crypto.defi.feature.settings.multichain

import com.crypto.defi.models.domain.ChainNetwork

data class MultiChainState(
  val selected: ChainNetwork = ChainNetwork.MAINNET,
  val supports: List<ChainNetwork> = emptyList(),
)
