package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.database.WalletDatabase
import com.easy.defi.app.core.model.data.Wallet
import javax.inject.Inject

class WalletRepository @Inject constructor(
  private val database: dagger.Lazy<WalletDatabase>,
) {
  suspend fun insertWallet(wallet: Wallet) {
//    database.get().insertWallet(wallet.asEntity())
  }

  suspend fun deleteOne(wallet: Wallet) {
//    database.get().deleteWallet(wallet.asEntity())
  }

  suspend fun activeOne(): Wallet? {
//    return database.get().activeWallet()?.asExternalModel()
    return null
  }
}
