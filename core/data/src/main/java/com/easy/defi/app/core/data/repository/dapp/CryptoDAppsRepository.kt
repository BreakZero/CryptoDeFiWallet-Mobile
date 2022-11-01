package com.easy.defi.app.core.data.repository.dapp

import com.easy.defi.app.core.model.data.DApp
import com.easy.defi.app.core.network.DAppRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CryptoDAppsRepository @Inject constructor(
  private val dAppRemoteDataSource: DAppRemoteDataSource
) : DAppRepository {
  override fun getRecommendDApps(): Flow<List<DApp>> {
    return flow { emit(dAppRemoteDataSource.loadAllDApps()) }
  }

  override fun getTops(topSize: Int): Flow<List<DApp>> {
    return emptyFlow()
  }
}
