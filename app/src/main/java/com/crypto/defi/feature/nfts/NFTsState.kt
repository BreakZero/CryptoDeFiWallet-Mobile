package com.crypto.defi.feature.nfts

import com.crypto.defi.models.remote.nft.NftAssetGroup

data class NFTsState(
    val isLoading: Boolean = true,
    val ntfs: List<NftAssetGroup> = emptyList()
)
