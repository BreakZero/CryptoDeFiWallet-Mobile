package com.crypto.wallet.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.crypto.core.ConfigurationKeys
import com.crypto.core.annotations.ExcludeFromJacocoGeneratedReport
import com.crypto.wallet.WalletRepository
import com.crypto.wallet.model.WalletDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import timber.log.Timber
import javax.inject.Singleton

@ExcludeFromJacocoGeneratedReport
@Module
@InstallIn(SingletonComponent::class)
object MultiWalletModule {
  @Provides
  @Singleton
  fun provideWalletDatabase(
      app: Application,
      sharedPreferences: SharedPreferences
  ): WalletDatabase {
    val passcode = sharedPreferences.getString(ConfigurationKeys.KEY_FOR_PASSCODE, "").orEmpty().also {
      Timber.v(it)
    }

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