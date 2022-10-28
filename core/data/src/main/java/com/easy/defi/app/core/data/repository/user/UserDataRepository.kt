package com.easy.defi.app.core.data.repository.user

import com.easy.defi.app.core.model.data.ChainNetwork
import com.easy.defi.app.core.model.data.DeFiCurrency
import com.easy.defi.app.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
  val userDataStream: Flow<UserData>

  suspend fun storePasscode(passcode: String)
  suspend fun setAvator(avatorUrl: String)
  suspend fun setWalletName(walletName: String)
  suspend fun setCurrency(currency: DeFiCurrency)
  suspend fun setNetwork(network: ChainNetwork)
}
