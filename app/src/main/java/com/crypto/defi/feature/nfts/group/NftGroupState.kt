package com.crypto.defi.feature.nfts.group

import com.crypto.defi.models.remote.nft.NftAssetGroup

data class NftGroupState(
    val isLoading: Boolean = true,
    val nftGroups: List<NftAssetGroup> = emptyList(),
)
