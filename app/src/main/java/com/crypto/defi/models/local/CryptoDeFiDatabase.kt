package com.crypto.defi.models.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crypto.defi.models.local.entities.ChainEntity
import com.crypto.defi.models.local.entities.CoinEntity

@Database(
    entities = [CoinEntity::class, ChainEntity::class],
    exportSchema = false,
    version = 1
)
abstract class CryptoDeFiDatabase : RoomDatabase() {

}
