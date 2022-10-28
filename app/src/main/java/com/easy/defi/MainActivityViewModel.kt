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

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.common.ConfigurationKeys
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import com.easy.defi.app.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val sharedPreferences: SharedPreferences,
  userDataRepository: UserDataRepository,
) : ViewModel() {
  private val hasWallet = !sharedPreferences.getString(ConfigurationKeys.KEY_FOR_PASSCODE, "").isNullOrBlank()

  val uiState: StateFlow<MainActivityUiState> = userDataRepository.userDataStream.map {
    MainActivityUiState.Success(hasWallet = hasWallet, userData = it)
  }.stateIn(
    scope = viewModelScope,
    initialValue = MainActivityUiState.Loading,
    started = SharingStarted.WhileSubscribed(5_000),
  )
}

sealed interface MainActivityUiState {
  object Loading : MainActivityUiState
  data class Success(val hasWallet: Boolean, val userData: UserData) : MainActivityUiState
}
