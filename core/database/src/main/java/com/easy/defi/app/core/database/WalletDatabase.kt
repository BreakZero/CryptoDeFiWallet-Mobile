package com.easy.defi.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.easy.defi.app.core.database.dao.WalletDao
import com.easy.defi.app.core.database.model.WalletEntity

@Database(
  entities = [WalletEntity::class],
  exportSchema = false,
  version = 1,
)
abstract class WalletDatabase : RoomDatabase() {
  abstract val walletDao: WalletDao
}