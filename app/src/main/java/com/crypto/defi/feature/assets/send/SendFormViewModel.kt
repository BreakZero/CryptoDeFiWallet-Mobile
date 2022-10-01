package com.crypto.defi.feature.assets.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
            symbol = asset.symbol,
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

    fun sign(
        onFailed:() -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _planState.update {
                TransactionPlan(
                    action = "ETH Transfer",
                    to = "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5",
                    from = "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5",
                    amount = "8.88",
                    symbol = "ETH",
                    fee = "0.00000023"
                )
            }
            onSuccess()
        }
    }
}