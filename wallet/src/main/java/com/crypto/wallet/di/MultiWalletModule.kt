package com.crypto.wallet.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.crypto.wallet.WalletRepository
import com.crypto.wallet.model.WalletDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton
import kotlin.text.toCharArray

@Module
@InstallIn(SingletonComponent::class)
object MultiWalletModule {
    @Provides
    @Singleton
    fun provideWalletDatabase(
        app: Application,
        sharedPreferences: SharedPreferences
    ): WalletDatabase {
        val passcode = sharedPreferences.getString("passcode", "").orEmpty()

        val passPhrase = SQLiteDatabase.getBytes(
            passcode.toCharArray()
        )
        val supportFactory = SupportFactory(passPhrase)
        return Room.databaseBuilder(app, WalletDatabase::class.java, "crypto_multi_wallet.db")
            .openHelperFactory(supportFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideWalletRepository(
        database: dagger.Lazy<WalletDatabase>
    ): WalletRepository {
        return WalletRepository(database)
    }
}