package com.easy.defi.app.core.data.repository.user

import com.easy.defi.app.core.datastore.UserPreferencesDataSource
import com.easy.defi.app.core.model.data.ChainNetwork
import com.easy.defi.app.core.model.data.DeFiCurrency
import com.easy.defi.app.core.model.data.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class OfflineUserDataRepository @Inject constructor(
  private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {
  override val userDataStream: Flow<UserData> = userPreferencesDataSource.userDataStream

  override suspend fun storePasscode(passcode: String) {
    userPreferencesDataSource.storePasscode(passcode)
  }

  override suspend fun setAvator(avatorUrl: String) {
    userPreferencesDataSource.setAvator(avatorUrl)
  }

  override suspend fun setWalletName(walletName: String) {
    userPreferencesDataSource.setWalletName(walletName)
  }

  override suspend fun setCurrency(currency: DeFiCurrency) {
    userPreferencesDataSource.setCurrency(currency)
  }

  override suspend fun setNetwork(network: ChainNetwork) {
    userPreferencesDataSource.setNetwork(network)
  }
}
