package com.easy.defi.app.core.database.di

import com.easy.defi.app.core.database.DeFiDatabase
import com.easy.defi.app.core.database.dao.AssetDao
import com.easy.defi.app.core.database.dao.ChainDao
import com.easy.defi.app.core.database.dao.CoinVersionDao
import com.easy.defi.app.core.database.dao.TierDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
  @Provides
  fun providesChainDao(
    database: DeFiDatabase,
  ): ChainDao = database.chainDao

  @Provides
  fun providesAssetDao(
    database: DeFiDatabase,
  ): AssetDao = database.assetDao

  @Provides
  fun providesTierDao(
    database: DeFiDatabase,
  ): TierDao = database.tierDao

  @Provides
  fun providesVersionDao(
    database: DeFiDatabase,
  ): CoinVersionDao = database.versionDao
}
