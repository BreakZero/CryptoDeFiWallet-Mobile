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

package com.easy.defi.app.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val userDataRepository: UserDataRepository
) : ViewModel() {
  val settingsState = userDataRepository.userDataStream.map {
    SettingsState(
      currency = it.currency,
      network = it.network,
      walletProfile = it.walletProfile
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SettingsState())

  fun updateWalletName() {
    viewModelScope.launch {
      userDataRepository.setWalletName("D_J")
      userDataRepository.setAvator(
        "https://logo.nftscan.com/logo/0xb16dfd9aaaf874fcb1db8a296375577c1baa6f21.png"
      )
    }
  }
}
