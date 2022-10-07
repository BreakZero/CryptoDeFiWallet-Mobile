package com.crypto.defi.models.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.crypto.defi.models.local.dao.AssetDao
import com.crypto.defi.models.local.dao.ChainDao
import com.crypto.defi.models.local.dao.CoinVersionDao
import com.crypto.defi.models.local.dao.TierDao
import com.crypto.defi.models.local.entities.AssetEntity
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.local.entities.CoinVersionShaEntity
import com.crypto.defi.models.local.entities.TierEntity

@Database(
    entities = [AssetEntity::class, ChainEntity::class, CoinVersionShaEntity::class, TierEntity::class],
    version = 2,
    autoMigrations = [
      AutoMigration(from = 1, to = 2)
    ]
)
abstract class CryptoDeFiDatabase : RoomDatabase() {
  abstract val chainDao: ChainDao
  abstract val assetDao: AssetDao
  abstract val versionDao: CoinVersionDao
  abstract val tierDao: TierDao
}
