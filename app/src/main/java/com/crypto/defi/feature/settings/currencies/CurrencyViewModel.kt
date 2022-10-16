package com.crypto.defi.feature.settings.currencies

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.models.domain.AppSettingsConfig
import com.crypto.defi.models.domain.DeFiCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
  private val appSettingsConfig: DataStore<AppSettingsConfig>
): ViewModel() {
  val currencyState = appSettingsConfig.data.map {
    CurrencyState(selected = it.currency)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CurrencyState())

  fun updateCurrency(currency: DeFiCurrency) {
    viewModelScope.launch {
      appSettingsConfig.updateData {
        it.copy(
          currency = currency
        )
      }
    }
  }
}