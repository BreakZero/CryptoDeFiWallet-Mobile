package com.easy.defi.app.core.data.repository.nft

import com.easy.defi.app.core.model.data.NftGroup
import com.easy.defi.app.core.model.data.NftInfo
import com.easy.defi.app.core.network.NftDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NftScanRepository @Inject constructor(
  private val nftDataSource: NftDataSource
) : NftRepository {
  override fun getGroupByErcType(ercType: String): Flow<List<NftGroup>> {
    return flow { emit(nftDataSource.groupOfType(ercType)) }
  }

  override fun getNftAssetByTokenId(contractAddress: String, tokenId: String): Flow<NftInfo> {
    return flow { emit(nftDataSource.getNftAssetByTokenId(contractAddress, tokenId)) }
  }
}
