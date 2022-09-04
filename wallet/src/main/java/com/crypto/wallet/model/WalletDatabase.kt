package com.crypto.wallet.model

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WalletEntity::class],
    exportSchema = false,
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract val walletDao: WalletDao

    init {
        Log.d("Hello", "init database")
    }
}
