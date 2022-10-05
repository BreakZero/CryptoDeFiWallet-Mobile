package com.crypto.defi.feature.nfts

import com.crypto.defi.models.remote.nft.AssetGroup

data class NFTsState(
    val isLoading: Boolean = true,
    val ntfs: List<AssetGroup> = emptyList()
)
