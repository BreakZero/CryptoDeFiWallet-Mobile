package com.crypto.defi.feature.nfts

import com.crypto.defi.models.domain.WalletNameInfo
import com.crypto.defi.models.remote.nft.NftInfo

data class NFTsState(
  val isLoading: Boolean = true,
  val walletNameInfo: WalletNameInfo = WalletNameInfo.Default,
  val nfts: List<NftInfo> = emptyList(),
)
