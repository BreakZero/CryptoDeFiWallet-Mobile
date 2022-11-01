package com.easy.defi.app.core.data.repository.dapp

interface DAppRepository {
  suspend fun getRecommendDApps(): List<String>
  suspend fun getTops(topSize: Int): List<String>
}
