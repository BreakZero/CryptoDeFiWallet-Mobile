package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.data.model.asEntity
import com.easy.defi.app.core.database.WalletDatabase
import com.easy.defi.app.core.database.model.asExternalModel
import com.easy.defi.app.core.model.data.Wallet
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class WalletRepository @Inject constructor(
  private val database: dagger.Lazy<WalletDatabase>,
) {
  suspend fun insertWallet(wallet: Wallet) {
    database.get().walletDao.insertWallet(wallet.asEntity())
  }

  suspend fun deleteOne(wallet: Wallet) {
    database.get().walletDao.deleteWallet(wallet.asEntity())
  }

  fun activeWalletStream(): Flow<Wallet> {
    return database.get().walletDao.activeWallet().filterNotNull().map {
      it.asExternalModel()
    }
  }
}
