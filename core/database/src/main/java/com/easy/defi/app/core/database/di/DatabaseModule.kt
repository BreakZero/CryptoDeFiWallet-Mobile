/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi.app.core.database.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.easy.defi.app.core.common.ConfigurationKeys
import com.easy.defi.app.core.database.DeFiDatabase
import com.easy.defi.app.core.database.WalletDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Provides
  @Singleton
  fun provideWalletDatabase(
    @ApplicationContext context: Context,
    sharedPreferences: SharedPreferences
  ): WalletDatabase {
    val passcode =
      sharedPreferences.getString(ConfigurationKeys.KEY_FOR_PASSCODE, "").orEmpty().also {
        Timber.tag("=====").v(it)
      }

    val passPhrase = SQLiteDatabase.getBytes(
      passcode.toCharArray()
    )
    val supportFactory = SupportFactory(passPhrase)
    return Room.databaseBuilder(context, WalletDatabase::class.java, "crypto_multi_wallet.db")
      .openHelperFactory(supportFactory)
      .build()
  }

  @Provides
  @Singleton
  fun provideDeFiDatabase(
    @ApplicationContext context: Context
  ): DeFiDatabase = Room.databaseBuilder(
    context,
    DeFiDatabase::class.java,
    "defi-database.db"
  ).build()
}
