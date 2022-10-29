package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.common.utils.DateTimeUtil
import com.easy.defi.app.core.data.model.asEntity
import com.easy.defi.app.core.database.dao.AssetDao
import com.easy.defi.app.core.database.dao.ChainDao
import com.easy.defi.app.core.database.dao.CoinVersionDao
import com.easy.defi.app.core.database.dao.TierDao
import com.easy.defi.app.core.database.model.AssetEntity
import com.easy.defi.app.core.database.model.ChainEntity
import com.easy.defi.app.core.database.model.CoinVersionShaEntity
import com.easy.defi.app.core.database.model.TierEntity
import com.easy.defi.app.core.database.model.asExternalModel
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.data.Chain
import com.easy.defi.app.core.model.data.Tier
import com.easy.defi.app.core.network.AssetDataSource
import com.easy.defi.app.core.network.model.CurrencyDto
import com.easy.defi.app.core.network.model.TierDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SupportCoinRepository @Inject constructor(
  private val chainDao: ChainDao,
  private val assetDao: AssetDao,
  private val versionDao: CoinVersionDao,
  private val tierDao: TierDao,
  private val assetDataSource: AssetDataSource
) : CoinRepository {
  override fun loadSupportCurrencies(): Flow<List<Asset>> {
    return assetDao.assetStream().map {
      it.map(AssetEntity::asExternalModel)
    }
  }

  override fun loadSupportChains(): Flow<List<Chain>> {
    return chainDao.chainStream().map {
      it.map(ChainEntity::asExternalModel)
    }
  }

  override fun loadTiers(toCurrency: String): Flow<List<Tier>> {
    return tierDao.tierStreamByCurrency(toCurrency).map { tiers ->
      tiers.map(TierEntity::asExternalModel)
    }
  }

  override suspend fun sync() {
    val lastVersion = versionDao.lastVersion()?.sha256.orEmpty()
    val networkCurrencies = assetDataSource.getCurrencies(lastVersion)
    if (lastVersion != networkCurrencies.sha256) {
      versionDao.insert(
        CoinVersionShaEntity(
          sha256 = networkCurrencies.sha256,
          createAt = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        )
      )
      assetDao.insertAll(networkCurrencies.currencies.map(CurrencyDto::asEntity))
    }

    val networkTiers = assetDataSource.getTiers()
    tierDao.insertAll(networkTiers.map(TierDto::asEntity))
  }
}
