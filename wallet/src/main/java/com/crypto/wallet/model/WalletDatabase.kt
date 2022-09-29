package com.crypto.wallet.model

import androidx.room.Database
import androidx.room.RoomDatabase
import timber.log.Timber

@Database(
    entities = [WalletEntity::class],
    exportSchema = false,
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract val walletDao: WalletDao

    init {
        Timber.v("init database")
    }
}
