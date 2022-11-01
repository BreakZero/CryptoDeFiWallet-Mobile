package com.easy.defi.app.core.data.repository.dapp

import com.easy.defi.app.core.model.data.DApp
import kotlinx.coroutines.flow.Flow

interface DAppRepository {
  fun getRecommendDApps(): Flow<List<DApp>>
  fun getTops(topSize: Int): Flow<List<DApp>>
}
