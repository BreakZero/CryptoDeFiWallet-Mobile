package com.easy.defi.app.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val userDataRepository: UserDataRepository,
) : ViewModel() {
  val settingsState = userDataRepository.userDataStream.map {
    SettingsState(
      currency = it.currency,
      network = it.network,
      walletProfile = it.walletProfile,
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SettingsState())

  fun updateWalletName() {
    viewModelScope.launch {
      userDataRepository.setWalletName("D&J")
      userDataRepository.setAvator("https://logo.nftscan.com/logo/0xb16dfd9aaaf874fcb1db8a296375577c1baa6f21.png")
    }
  }
}
