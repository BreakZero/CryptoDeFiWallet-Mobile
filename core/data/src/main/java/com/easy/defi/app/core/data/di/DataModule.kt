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

import com.easy.defi.app.core.data.HdWalletHolder
import com.easy.defi.app.core.data.di.annotations.Bitcoin
import com.easy.defi.app.core.data.di.annotations.Ethereum
import com.easy.defi.app.core.data.repository.CoinRepository
import com.easy.defi.app.core.data.repository.SupportCoinRepository
import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.data.repository.chain.BitcoinRepository
import com.easy.defi.app.core.data.repository.chain.ChainRepository
import com.easy.defi.app.core.data.repository.chain.EthereumRepository
import com.easy.defi.app.core.data.repository.dapp.CryptoDAppsRepository
import com.easy.defi.app.core.data.repository.dapp.DAppRepository
import com.easy.defi.app.core.data.repository.nft.NftRepository
import com.easy.defi.app.core.data.repository.nft.NftScanRepository
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
  @Ethereum
  fun bindsEthereumChainRepository(
    evmChainRepository: EthereumRepository
  ): ChainRepository

  @Binds
  @Bitcoin
  fun bindsBitcoinChainRepository(
    bitcoinChainRepository: BitcoinRepository
  ): ChainRepository

  @Binds
  fun bindsUserRepository(
    userDataRepository: OfflineUserDataRepository
  ): UserDataRepository

  @Binds
  fun bindsCoinRepository(
    supportChainRepository: SupportCoinRepository
  ): CoinRepository

  @Binds
  fun bindsCryptoDAppRepository(
    cryptoDAppsRepository: CryptoDAppsRepository
  ): DAppRepository

  @Binds
  fun bindsNftScanRepository(
    nftScanRepository: NftScanRepository
  ): NftRepository

  @Binds
  fun bindsNetworkMonitor(
    networkMonitor: ConnectivityManagerNetworkMonitor
  ): NetworkMonitor
}

@Module
@InstallIn(SingletonComponent::class)
object WalletModule {
  @Provides
  @Singleton
  fun provideWalletRepository(
    database: dagger.Lazy<WalletDatabase>
  ): WalletRepository {
    return WalletRepository(database)
  }

  @Provides
  @Singleton
  fun provideHdWalletHolder(): HdWalletHolder {
    return HdWalletHolder()
  }
}
