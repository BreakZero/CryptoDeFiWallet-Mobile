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

package com.easy.defi.app.core.data.di

import com.easy.defi.app.core.data.repository.ChainRepository
import com.easy.defi.app.core.data.repository.EvmChainRepository
import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.data.repository.user.OfflineUserDataRepository
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import com.easy.defi.app.core.data.util.ConnectivityManagerNetworkMonitor
import com.easy.defi.app.core.data.util.NetworkMonitor
import com.easy.defi.app.core.database.WalletDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
  @Binds
  fun bindsChainRepository(
    evmChainRepository: EvmChainRepository,
  ): ChainRepository

  @Binds
  fun bindsUserRepository(
    userDataRepository: OfflineUserDataRepository,
  ): UserDataRepository

  @Binds
  fun bindsNetworkMonitor(
    networkMonitor: ConnectivityManagerNetworkMonitor,
  ): NetworkMonitor
}

@Module
@InstallIn(SingletonComponent::class)
object WalletModule {
  @Provides
  @Singleton
  fun provideWalletRepository(
    database: dagger.Lazy<WalletDatabase>,
  ): WalletRepository {
    return WalletRepository(database)
  }
}
