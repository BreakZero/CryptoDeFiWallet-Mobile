package com.easy.defi.app.core.database

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.easy.defi.app.core.common.ConfigurationKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Provides
  @Singleton
  fun provideWalletDatabase(
    app: Application,
    sharedPreferences: SharedPreferences,
  ): WalletDatabase {
    val passcode = sharedPreferences.getString(ConfigurationKeys.KEY_FOR_PASSCODE, "").orEmpty()

    val passPhrase = SQLiteDatabase.getBytes(
      passcode.toCharArray(),
    )
    val supportFactory = SupportFactory(passPhrase)
    return Room.databaseBuilder(app, WalletDatabase::class.java, "crypto_multi_wallet.db")
      .openHelperFactory(supportFactory)
      .build()
  }

  /*@Provides
  @Singleton
  fun provideWalletRepository(
    database: dagger.Lazy<WalletDatabase>,
  ): WalletRepository {
    return WalletRepository(database)
  }*/
}
