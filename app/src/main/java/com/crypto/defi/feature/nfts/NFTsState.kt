package com.crypto.defi.feature.nfts

import com.crypto.defi.models.remote.nft.NftInfo

data class NFTsState(
  val isLoading: Boolean = true,
  val nfts: List<NftInfo> = emptyList()
)
