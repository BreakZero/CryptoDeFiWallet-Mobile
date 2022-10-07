package com.crypto.defi.feature.assets.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.extensions.orElse
import com.crypto.core.extensions.upWithDecimal
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SendFormViewModel @AssistedInject constructor(
    private val chainManager: ChainManager,
    assetUseCase: AssetUseCase,
    @Assisted private val coinSlug: String
) : ViewModel() {
  private val _asset = assetUseCase.findAssetBySlug(coinSlug).filterNotNull()
  private val _to = MutableStateFlow("")
  private val _amount = MutableStateFlow("")
  private val _memo = MutableStateFlow("")

  private val _planState = MutableStateFlow(TransactionPlan.EmptyPlan)

  val sendFormState = combine(
      _asset,
      _planState,
      _to,
      _amount,
      _memo
  ) { asset, plan, to, amount, memo ->
    SendFormState(
        asset = asset,
        to = to,
        amount = amount,
        memo = memo,
        plan = plan
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SendFormState())

  fun onToChanged(newTo: String) {
    _to.update { newTo }
  }

  fun onAmountChanged(newAmount: String) {
    _amount.update { newAmount }
  }

  fun onMemoChanged(newMemo: String) {
    _memo.update { newMemo }
  }

  fun clearPlan() {
    _planState.update {
      TransactionPlan.EmptyPlan
    }
  }

  fun sign(
      onSuccess: () -> Unit,
      onFailed: (String) -> Unit
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      with(sendFormState.value) {
        this.asset?.also {
          val iChain = chainManager.getChainByKey(it.code)
          try {
            val plan = iChain.signTransaction(
                ReadyToSign(
                    to = this.to,
                    memo = this.memo.ifBlank { null },
                    contract = it.contract,
                    amount = this.amount.toBigDecimal()
                        .upWithDecimal(asset.decimal)
                )
            )
            _planState.update {
              plan
            }
            onSuccess()
          } catch (e: Exception) {
            onFailed.invoke(e.message.orElse("something went wrong..."))
          }
        }
      }
    }
  }

  fun broadcast(
      onFailed: (String) -> Unit,
      onSuccess: () -> Unit
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      with(sendFormState.value) {
        this.asset?.also {
          val iChain = chainManager.getChainByKey(it.code)
          try {
            iChain.broadcast(this.plan.rawData)
            onSuccess()
          } catch (e: Exception) {
            e.printStackTrace()
            onFailed.invoke(e.message.orElse("something went wrong..."))
          }
        }
      }
    }
  }
}