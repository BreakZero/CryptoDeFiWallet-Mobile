package com.easy.defi.app.settings.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.data.repository.user.OfflineUserDataRepository
import com.easy.defi.app.core.model.data.DeFiCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SetCurrencyViewModel @Inject constructor(
  private val offlineUserDataRepository: OfflineUserDataRepository
) : ViewModel() {
  val currencyState = offlineUserDataRepository.userDataStream.map {
    CurrencyUiState(
      selected = it.currency
    )
  }.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(),
    CurrencyUiState(
      selected = with(Currency.getInstance(Locale.US)) {
        DeFiCurrency(
          symbol = symbol,
          code = currencyCode
        )
      }
    )
  )

  fun updateCurrency(currency: DeFiCurrency) {
    viewModelScope.launch {
      offlineUserDataRepository.setCurrency(currency)
    }
  }
}

data class CurrencyUiState(
  val selected: DeFiCurrency,
  val supportList: List<DeFiCurrency> = Currency.getAvailableCurrencies().map {
    DeFiCurrency(code = it.currencyCode, symbol = it.symbol)
  }
)
