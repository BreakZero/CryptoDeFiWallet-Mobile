package com.crypto.defi.feature.settings

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.models.domain.AppSettingsConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val appSettingsConfig: DataStore<AppSettingsConfig>
) : ViewModel() {
  val settingsState = appSettingsConfig.data.map {
    SettingsState(
      currency = it.currency,
      network = it.network,
      walletNameInfo = it.walletNameInfo
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SettingsState())

  fun updateWalletName() {
    viewModelScope.launch {
      appSettingsConfig.updateData {
        it.copy(
          walletNameInfo = it.walletNameInfo.copy(
            walletName = "D&J",
            avator = "https://logo.nftscan.com/logo/0xb16dfd9aaaf874fcb1db8a296375577c1baa6f21.png"
          )
        )
      }
    }
  }
}