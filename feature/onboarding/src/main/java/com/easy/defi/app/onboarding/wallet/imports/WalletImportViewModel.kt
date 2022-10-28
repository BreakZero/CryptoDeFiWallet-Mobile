package com.easy.defi.app.onboarding.wallet.imports

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.common.extensions.launchWithHandler
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import com.easy.defi.app.core.domain.InsertWalletUseCase
import com.easy.defi.app.core.ui.UiEvent
import com.easy.defi.app.core.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WalletImportViewModel @Inject constructor(
  private val userDataRepository: UserDataRepository,
  private val insertWalletUseCase: InsertWalletUseCase,
) : ViewModel() {
  var state by mutableStateOf(ImportState())
    private set

  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  fun onEvent(event: ImportEvent) {
    when (event) {
      is ImportEvent.OnImportClick -> {
        viewModelScope.launchWithHandler {
          insertWalletUseCase(
            mnemonic = state.phrase,
            doFirst = {
              userDataRepository.storePasscode(event.passcode)
            },
            doLast = {
              if (it) {
                state = state.copy(inProgress = false)
                _uiEvent.send(UiEvent.Success)
              } else {
                _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("invalid mnemonic, please try another")))
              }
            }
          )
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
