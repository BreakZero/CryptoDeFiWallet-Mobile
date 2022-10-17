package com.crypto.defi.feature.assets

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.crypto.core.common.UiText
import com.crypto.defi.chains.usecase.AssetUseCase
import com.crypto.defi.models.domain.AppSettingsConfig
import com.crypto.defi.models.mapper.toAsset
import com.crypto.defi.workers.BalanceWorker
import com.crypto.resource.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainAssetsViewModel @Inject constructor(
  private val appSettingsConfig: DataStore<AppSettingsConfig>,
  private val assetUseCase: AssetUseCase,
  private val workManager: WorkManager
) : ViewModel() {

  companion object {
    private const val WORKER_NAME = "update-balance-worker"
    const val KEY_WORKER_PROGRESS = "in_progressing"
  }

  private val balanceWorkerRequest = PeriodicWorkRequestBuilder<BalanceWorker>(
    15, TimeUnit.MINUTES
  ).setId(UUID.fromString("d5b42914-cb0f-442c-a800-1532f52a5ed8"))
    .setConstraints(
      Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    ).build()

  init {
    viewModelScope.launch(Dispatchers.IO) {
      assetUseCase.fetchingAssets {
        workManager.enqueueUniquePeriodicWork(
          WORKER_NAME,
          ExistingPeriodicWorkPolicy.UPDATE,
          balanceWorkerRequest
        )
        withContext(Dispatchers.Main) {
          workManager.getWorkInfoByIdLiveData(balanceWorkerRequest.id).observeForever {
            val inProgress = it.progress.getBoolean(KEY_WORKER_PROGRESS, false)
            _isLoading.update {
              inProgress
            }
          }
        }
      }
    }
  }

  private val _isLoading = MutableStateFlow(true)
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
    assetUseCase.assetsFlow(),
    assetUseCase.tiersFlow(),
    appSettingsConfig.data,
    _promoCards
  ) { isLoading, assets, tiers, appSettings, promoCards ->
    val localAssets = assets.map { asset ->
      val rate = tiers.find { tier ->
        asset.slug == tier.fromSlug
      }?.rate ?: "0.0"
      asset.toAsset().copy(
        rate = rate.toBigDecimalOrNull() ?: BigDecimal.ZERO
      )
    }
    MainAssetState(
      onRefreshing = isLoading,
      walletNameInfo = appSettings.walletNameInfo,
      assets = localAssets.filter {
        it.nativeBalance > BigInteger.ZERO
      }.sortedByDescending { it.fiatBalance() },
      totalBalance = localAssets.sumOf { it.fiatBalance() }.toPlainString(),
      promoCard = promoCards
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(),
    MainAssetState(promoCard = emptyList())
  )

  fun onRefresh() {
    _isLoading.update { true }
    workManager.enqueueUniquePeriodicWork(
      WORKER_NAME,
      ExistingPeriodicWorkPolicy.UPDATE,
      balanceWorkerRequest
    )
  }
}
