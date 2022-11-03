package com.easy.defi.app.core.data.paging

import com.easy.defi.app.core.network.NftDataSource
import javax.inject.Inject

class NftPagingSource @Inject constructor(
  private val nftDataSource: NftDataSource
)
