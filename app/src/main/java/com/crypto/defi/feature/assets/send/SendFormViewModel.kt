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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SendFormViewModel @AssistedInject constructor(
    private val chainManager: ChainManager,
    assetUseCase: AssetUseCase,
    @Assisted private val coinSlug: String
): ViewModel() {
    private val _asset = assetUseCase.findAssetBySlug(coinSlug).filterNotNull()
    private val _sendForm = MutableStateFlow(SendFormInfo())

    private val _planState = MutableStateFlow(TransactionPlan.EmptyPlan)

    val sendFormState = combine(_asset, _sendForm, _planState) { asset, form, plan ->
        SendFormState(
            asset = asset,
            formInfo = form,
            plan = plan
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SendFormState())

    fun onToChanged(newTo: String) {
        _sendForm.update {
            it.copy(to = newTo)
        }
    }
    fun onAmountChanged(newAmount: String) {
        _sendForm.update {
            it.copy(amount = newAmount)
        }
    }

    fun onMemoChanged(newMemo: String) {
        _sendForm.update {
            it.copy(memo = newMemo)
        }
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
                                to = this.formInfo.to,
                                memo = this.formInfo.memo.ifBlank { null },
                                contract = it.contract,
                                amount = this.formInfo.amount.toBigDecimal().upWithDecimal(asset.decimal)
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