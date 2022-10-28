package com.easy.defi.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.easy.defi.app.core.database.dao.AssetDao
import com.easy.defi.app.core.database.dao.ChainDao
import com.easy.defi.app.core.database.dao.CoinVersionDao
import com.easy.defi.app.core.database.dao.TierDao
import com.easy.defi.app.core.database.model.AssetEntity
import com.easy.defi.app.core.database.model.ChainEntity
import com.easy.defi.app.core.database.model.CoinVersionShaEntity
import com.easy.defi.app.core.database.model.TierEntity

@Database(
  entities = [AssetEntity::class, ChainEntity::class, CoinVersionShaEntity::class, TierEntity::class],
  exportSchema = false,
  version = 1
)
abstract class DeFiDatabase : RoomDatabase() {
  abstract val chainDao: ChainDao
  abstract val assetDao: AssetDao
  abstract val versionDao: CoinVersionDao
  abstract val tierDao: TierDao
}
