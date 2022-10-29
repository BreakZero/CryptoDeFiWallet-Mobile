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
import com.easy.defi.app.core.data.repository.user.OfflineUserDataRepository
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class AssetListViewModel @Inject constructor(
  offlineUserDataRepository: OfflineUserDataRepository,
  supportCoinRepository: CoinRepository
) : ViewModel() {

  companion object {
    private const val WORKER_NAME = "update-balance-worker"
    const val KEY_WORKER_PROGRESS = "in_progressing"
  }

  init {
    viewModelScope.launchWithHandler {
      supportCoinRepository.sync()
    }
  }

/*  private val balanceWorkerRequest = PeriodicWorkRequestBuilder<BalanceWorker>(
    15,
    TimeUnit.MINUTES,
  ).setId(UUID.fromString("d5b42914-cb0f-442c-a800-1532f52a5ed8"))
    .setConstraints(
      Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build(),
    ).build()*/

  private val _isLoading = MutableStateFlow(false)
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
    _isLoading,
    offlineUserDataRepository.userDataStream,
    supportCoinRepository.loadSupportCurrencies(),
    _promoCards
  ) { isLoading, userData, assets, promoCards ->
    AssetListState(
      onRefreshing = isLoading,
      assets = assets.sortedByDescending { it.nativeBalance }.filter { it.nativeBalance > BigInteger.ZERO },
      walletProfile = userData.walletProfile,
      promoCard = promoCards
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(),
    AssetListState(promoCard = emptyList())
  )

  fun onRefresh() {
    _isLoading.update { true }
  }
}
