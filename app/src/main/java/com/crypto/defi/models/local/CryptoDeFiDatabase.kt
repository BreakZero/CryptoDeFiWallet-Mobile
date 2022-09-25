package com.crypto.defi.models.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crypto.defi.models.local.dao.AssetDao
import com.crypto.defi.models.local.dao.ChainDao
import com.crypto.defi.models.local.dao.CoinVersionDao
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.local.entities.AssetEntity
import com.crypto.defi.models.local.entities.CoinVersionShaEntity

@Database(
    entities = [AssetEntity::class, ChainEntity::class, CoinVersionShaEntity::class],
    exportSchema = false,
    version = 1
)
abstract class CryptoDeFiDatabase : RoomDatabase() {
    abstract val chainDao: ChainDao
    abstract val assetDao: AssetDao
    abstract val versionDao: CoinVersionDao
}
