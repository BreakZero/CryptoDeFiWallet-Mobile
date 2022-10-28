package com.easy.defi.app.core.data.repository

interface CoinRepository {
  suspend fun loadSupportCurrencies(): List<String>
  suspend fun loadSupportChains(): List<String>
  suspend fun loadTiers(): List<String>
}
