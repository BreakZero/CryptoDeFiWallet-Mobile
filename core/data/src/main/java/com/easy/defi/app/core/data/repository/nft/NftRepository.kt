package com.easy.defi.app.core.data.repository.nft

import com.easy.defi.app.core.model.data.NftGroup
import com.easy.defi.app.core.model.data.NftInfo
import kotlinx.coroutines.flow.Flow

interface NftRepository {
  fun getGroupByErcType(address: String, ercType: String): Flow<List<NftGroup>>

  fun getNftAssetByTokenId(contractAddress: String, tokenId: String): Flow<NftInfo>
}
