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

package com.easy.defi.feature.asset.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.common.extensions.launchWithHandler
import com.easy.defi.app.core.data.repository.CoinRepository
import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.data.repository.user.OfflineUserDataRepository
import com.easy.defi.app.core.data.util.SyncStatusMonitor
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class AssetListViewModel @Inject constructor(
  walletRepository: WalletRepository,
  offlineUserDataRepository: OfflineUserDataRepository,
  supportCoinRepository: CoinRepository,
  private val syncStatusMonitor: SyncStatusMonitor
) : ViewModel() {
  private val walletJob: Job
  init {
    walletJob = viewModelScope.launchWithHandler {
      supportCoinRepository.sync()
      walletRepository.activeWalletStream().collectLatest { wallet ->
        wallet?.also {
          syncStatusMonitor.startUp()
        }
      }
    }
  }

  private val isSyncing = syncStatusMonitor.isSyncing
  private val _promoCards = MutableStateFlow(
    listOf(
      PromoCard(
        backgroundRes = R.drawable.card_small_orange,
        title = UiText.StringResource(R.string.new_coins__new_coin)
      ),
      PromoCard(
        backgroundRes = R.drawable.card_small_black,
        title = UiText.StringResource(R.string.wallet_asset__get_eth_ready_for_gas_fees)
      ),
      PromoCard(
        backgroundRes = R.drawable.card_small_purple,
        title = UiText.StringResource(R.string.wallet_asset__enable_email)
      )
    )
  )

  val assetState = combine(
    isSyncing,
    offlineUserDataRepository.userDataStream,
    supportCoinRepository.loadSupportCurrencies(),
    _promoCards
  ) { isLoading, userData, assets, promoCards ->
    AssetListState(
      onRefreshing = isLoading,
      assets = assets.sortedByDescending { it.nativeBalance }
        .filter { it.nativeBalance > BigInteger.ZERO },
      walletProfile = userData.walletProfile,
      promoCard = promoCards
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(),
    AssetListState(promoCard = emptyList())
  )

  fun onRefresh() {
    syncStatusMonitor.startUp()
  }

  override fun onCleared() {
    super.onCleared()
    walletJob.cancel()
  }
}
