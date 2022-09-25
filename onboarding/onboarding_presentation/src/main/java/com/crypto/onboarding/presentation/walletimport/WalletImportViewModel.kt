package com.crypto.onboarding.presentation.walletimport

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.ConfigurationKeys
import com.crypto.core.common.UiEvent
import com.crypto.core.common.UiText
import com.crypto.wallet.WalletRepository
import com.crypto.wallet.model.WalletEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wallet.core.jni.Mnemonic
import javax.inject.Inject

@HiltViewModel
class WalletImportViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val walletRepository: WalletRepository
) : ViewModel() {
    var state by mutableStateOf(ImportState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ImportEvent) {
        when (event) {
            is ImportEvent.OnImportClick -> {
                val isValid = Mnemonic.isValid(state.phrase)
                if (isValid) {
                    state = state.copy(inProgress = true)
                    preferences.edit {
                        putString(ConfigurationKeys.KEY_FOR_PASSCODE, event.passcode)
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        delay(1000L)
                        walletRepository.insertWallet(
                            /*WalletEntity(
                                mnemonic = state.phrase, 1, passphrase = ""
                            )*/
                            WalletEntity(
                                mnemonic = state.phrase,
                                1,
                                passphrase = ""
                            )
                        )
                        state = state.copy(inProgress = false)
                        _uiEvent.send(UiEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("invalid mnemonic, please try another")))
                    }
                }
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
