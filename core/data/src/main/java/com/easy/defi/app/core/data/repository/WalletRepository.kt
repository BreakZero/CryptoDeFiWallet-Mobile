package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.data.model.asEntity
import com.easy.defi.app.core.database.dao.WalletDao
import com.easy.defi.app.core.database.model.asExternalModel
import com.easy.defi.app.core.model.Wallet
import javax.inject.Inject

class WalletRepository @Inject constructor(
  private val database: dagger.Lazy<WalletDao>,
) {
  suspend fun insertWallet(wallet: Wallet) {
    database.get().insertWallet(wallet.asEntity())
  }

  suspend fun deleteOne(wallet: Wallet) {
    database.get().deleteWallet(wallet.asEntity())
  }

  suspend fun activeOne(): Wallet? {
    return database.get().activeWallet()?.asExternalModel()
  }
}
