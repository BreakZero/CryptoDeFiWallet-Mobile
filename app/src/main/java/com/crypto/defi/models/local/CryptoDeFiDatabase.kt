package com.crypto.defi.models.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
  version = 3,
  autoMigrations = [
    AutoMigration(from = 1, to = 2),
  ],
)
abstract class CryptoDeFiDatabase : RoomDatabase() {
  abstract val chainDao: ChainDao
  abstract val assetDao: AssetDao
  abstract val versionDao: CoinVersionDao
  abstract val tierDao: TierDao

  object Migrations {
    val MIGRATION_2_3 = object : Migration(2, 3) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE TB_CHAIN ADD COLUMN is_token INTEGER DEFAULT 0 NOT NULL")
      }
    }
  }
}
