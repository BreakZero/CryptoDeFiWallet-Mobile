package com.crypto.defi.feature.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.crypto.core.common.UiText
import com.crypto.defi.chains.usecase.AssetUseCase
import com.crypto.defi.models.mapper.toAsset
import com.crypto.defi.workers.BalanceWorker
import com.crypto.resource.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainAssetsViewModel @Inject constructor(
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

  var assetState by mutableStateOf(
    MainAssetState(
      promoCard = listOf(
        PromoCart(
          backgroundRes = R.drawable.card_small_orange,
          title = UiText.StringResource(R.string.new_coins__new_coin)
        ),
        PromoCart(
          backgroundRes = R.drawable.card_small_black,
          title = UiText.StringResource(R.string.wallet_asset__get_eth_ready_for_gas_fees)
        ),
        PromoCart(
          backgroundRes = R.drawable.card_small_purple,
          title = UiText.StringResource(R.string.wallet_asset__enable_email)
        )
      )
    )
  )
    private set


  init {
    viewModelScope.launch(Dispatchers.IO) {
      assetUseCase.fetchingAssets {
        withContext(Dispatchers.Main) {
          workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            balanceWorkerRequest
          )
          workManager.getWorkInfoByIdLiveData(balanceWorkerRequest.id).observeForever {
            val inProgress = it.progress.getBoolean(KEY_WORKER_PROGRESS, false)
            assetState = assetState.copy(onRefreshing = inProgress)
          }
        }
        combine(
          assetUseCase.assetsFlow(),
          assetUseCase.tiersFlow()
        ) { assets, tiers ->
          assets.map { asset ->
            val rate = tiers.find { tier ->
              asset.slug == tier.fromSlug
            }?.rate ?: "0.0"
            asset.toAsset().copy(
              rate = rate.toBigDecimalOrNull() ?: BigDecimal.ZERO
            )
          }
        }.collect { assets ->
          assetState = assetState.copy(
            onRefreshing = false,
            assets = assets.filter {
              it.nativeBalance > BigInteger.ZERO
            }.sortedByDescending { it.fiatBalance() },
            totalBalance = assets.sumOf { it.fiatBalance() }.toPlainString()
          )
        }
      }
    }
  }

  fun onRefresh() {
    assetState = assetState.copy(
      onRefreshing = true
    )
    workManager.enqueueUniquePeriodicWork(
      WORKER_NAME,
      ExistingPeriodicWorkPolicy.UPDATE,
      balanceWorkerRequest
    )
  }
}
