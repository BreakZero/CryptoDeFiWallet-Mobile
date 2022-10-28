package com.easy.defi.feature.asset.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.data.repository.user.OfflineUserDataRepository
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class AssetListViewModel @Inject constructor(
  private val offlineUserDataRepository: OfflineUserDataRepository,
) : ViewModel() {

  companion object {
    private const val WORKER_NAME = "update-balance-worker"
    const val KEY_WORKER_PROGRESS = "in_progressing"
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
        title = UiText.StringResource(R.string.new_coins__new_coin),
      ),
      PromoCard(
        backgroundRes = R.drawable.card_small_black,
        title = UiText.StringResource(R.string.wallet_asset__get_eth_ready_for_gas_fees),
      ),
      PromoCard(
        backgroundRes = R.drawable.card_small_purple,
        title = UiText.StringResource(R.string.wallet_asset__enable_email),
      ),
    ),
  )

  val assetState = combine(
    _isLoading,
    offlineUserDataRepository.userDataStream,
    _promoCards,
  ) { isLoading, userData, promoCards ->
    AssetListState(
      onRefreshing = isLoading,
      walletProfile = userData.walletProfile,
      promoCard = promoCards,
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(),
    AssetListState(promoCard = emptyList()),
  )

  fun onRefresh() {
    _isLoading.update { true }
  }
}
