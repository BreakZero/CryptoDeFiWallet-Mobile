package com.crypto.onboarding.presentation.walletimport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.common.UiEvent
import com.crypto.core.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletImportViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(ImportState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ImportEvent) {
        when (event) {
            ImportEvent.OnImportClick -> {
                /*kotlin.runCatching {
                    HDWallet(state.phrase, "")
                }.onFailure {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("invalid mnemonic, please try another")))
                    }
                }.onSuccess {
                    walletRepository.inject(it)
                    viewModelScope.launch {
                        walletRepository.insertWallet(
                            WalletEntity(
                                mnemonic = state.phrase, 1, passphrase = ""
                            )
                        )
                        _uiEvent.send(UiEvent.Success)
                    }
                }*/
            }
            is ImportEvent.OnFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.phrase.isBlank()
                )
            }
            is ImportEvent.OnPhraseChange -> {
                state = state.copy(phrase = event.phrase)
            }
        }
    }

    fun onNavigateUp() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}
