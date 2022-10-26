package com.crypto.defi.feature.settings.currencies

import com.crypto.defi.models.domain.DeFiCurrency
import java.util.*

data class CurrencyState(
  val selected: DeFiCurrency = Currency.getInstance(Locale.US).let {
    DeFiCurrency(
      symbol = it.symbol,
      code = it.currencyCode,
    )
  },
  val supportList: List<DeFiCurrency> = Currency.getAvailableCurrencies().map {
    DeFiCurrency(code = it.currencyCode, symbol = it.symbol)
  },
)
