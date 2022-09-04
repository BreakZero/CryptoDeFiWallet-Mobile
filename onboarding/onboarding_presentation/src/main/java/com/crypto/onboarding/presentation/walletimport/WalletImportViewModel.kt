package com.crypto.onboarding.presentation.walletimport

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.withContext
import wallet.core.jni.HDWallet
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
                kotlin.runCatching {
//                    HDWallet(state.phrase, "")
                    HDWallet("ripple scissors kick mammal hire column oak again sun offer wealth tomorrow wagon turn fatal", "")
                }.onFailure {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("invalid mnemonic, please try another")))
                    }
                }.onSuccess {
                    preferences.edit {
                        putString("passcode", event.passcode)
                    }
                    walletRepository.inject(it)
                    viewModelScope.launch {
                        delay(1000L)
                        walletRepository.insertWallet(
                            WalletEntity(
                                mnemonic = state.phrase, 1, passphrase = ""
                            )
                        )
                        _uiEvent.send(UiEvent.Success)
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
            withContext(Dispatchers.IO) {
                walletRepository.activeOne()?.also {
                    Log.d("=====", it.mnemonic)
                }
                _uiEvent.send(UiEvent.NavigateUp)
            }
        }
    }
}
