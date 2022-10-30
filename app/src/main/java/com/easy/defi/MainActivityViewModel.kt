/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.data.HdWalletHolder
import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import com.easy.defi.app.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  hdWalletHolder: HdWalletHolder,
  userDataRepository: UserDataRepository,
  walletRepository: WalletRepository
) : ViewModel() {
  private var walletJob: Job? = null

  init {
    walletJob = userDataRepository.userDataStream.flatMapConcat {
      if (it.hasPasscode) {
        walletRepository.activeWalletStream()
      } else {
        emptyFlow()
      }
    }.onEach {
      Timber.tag("======").v(it.toString())
      hdWalletHolder.inject(it.mnemonic, it.passphrase)
    }.launchIn(viewModelScope)
  }

  val uiState: StateFlow<MainActivityUiState> = userDataRepository.userDataStream.map {
    delay(1000)
    MainActivityUiState.Success(userData = it)
  }.stateIn(
    scope = viewModelScope,
    initialValue = MainActivityUiState.Loading,
    started = SharingStarted.WhileSubscribed()
  )

  override fun onCleared() {
    super.onCleared()
    walletJob?.cancel()
  }
}

sealed interface MainActivityUiState {
  object Loading : MainActivityUiState
  data class Success(val userData: UserData) : MainActivityUiState
}
