package com.crypto.defi.models.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crypto.defi.models.local.dao.AssetDao
import com.crypto.defi.models.local.dao.ChainDao
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.local.entities.AssetEntity

@Database(
    entities = [AssetEntity::class, ChainEntity::class],
    exportSchema = false,
    version = 1
)
abstract class CryptoDeFiDatabase : RoomDatabase() {
    abstract val chainDao: ChainDao
    abstract val assetDao: AssetDao
}
