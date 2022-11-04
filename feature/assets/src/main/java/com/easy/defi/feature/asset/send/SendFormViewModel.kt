package com.easy.defi.feature.asset.send

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.common.extensions.launchWithHandler
import com.easy.defi.app.core.data.repository.CoinRepository
import com.easy.defi.app.core.data.repository.chain.ChainManager
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.data.ReadyToSign
import com.easy.defi.app.core.model.data.TransactionPlan
import com.easy.defi.app.core.model.x.upWithDecimal
import com.easy.defi.feature.asset.send.navigation.TransactionSendArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class SendFormViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  stringDecoder: StringDecoder,
  coinRepository: CoinRepository,
  private val chainManager: ChainManager
) : ViewModel() {

  private val slugArgs = TransactionSendArgs(savedStateHandle, stringDecoder)

  private val _asset = coinRepository.assetBySlug(slugArgs.slug)
  private val _to = MutableStateFlow("")
  private val _memo = MutableStateFlow("")
  private val _amount = MutableStateFlow("")
  private val _planState = MutableStateFlow(TransactionPlan.EmptyPlan)

  val sendFormState = combine(
    _asset,
    _to,
    _memo,
    _amount,
    _planState
  ) { asset, to, memo, amount, plan ->
    SendFormState(
      asset = asset,
      to = to,
      memo = memo,
      amount = amount,
      plan = plan
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), SendFormState())

  fun onToChanged(to: String) {
    _to.update { to }
  }

  fun onMemoChanged(memo: String) {
    _memo.update { memo }
  }

  fun onAmountChanged(amount: String) {
    _amount.update { amount }
  }

  fun releasePlan() {
    _planState.update { TransactionPlan.EmptyPlan }
  }

  fun sign() {
    sendFormState.value.let { state ->
      state.asset?.let { asset ->
        viewModelScope.launchWithHandler {
          val plan = chainManager.getChainByAsset(asset)?.sign(
            ReadyToSign(
              to = state.to,
              memo = state.memo.ifBlank { null },
              contract = asset.contract,
              amount = state.amount?.toBigDecimal()?.upWithDecimal(asset.decimal) ?: BigInteger.ZERO
            )
          )
          plan?.let {
            Timber.tag("=====").v(it.toString())
            _planState.update { it }
            // on success
          } ?: kotlin.run {
            // on failure
            _planState.update { TransactionPlan.EmptyPlan }
          }
        }
      }
    }
  }
}

data class SendFormState(
  val asset: Asset? = null,
  val to: String = "",
  val amount: String? = null,
  val memo: String = "",
  val plan: TransactionPlan = TransactionPlan.EmptyPlan
)
