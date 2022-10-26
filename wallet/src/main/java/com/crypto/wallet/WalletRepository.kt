package com.crypto.wallet

import com.crypto.wallet.model.Wallet
import com.crypto.wallet.model.WalletDatabase
import com.crypto.wallet.model.toWallet
import com.crypto.wallet.model.toWalletEntity
import javax.inject.Inject

class WalletRepository @Inject constructor(
  private val database: dagger.Lazy<WalletDatabase>,
) {
  suspend fun insertWallet(wallet: Wallet) {
    database.get().walletDao.insertWallet(wallet.toWalletEntity())
  }

  suspend fun deleteOne(wallet: Wallet) {
    database.get().walletDao.deleteWallet(wallet.toWalletEntity())
  }

  suspend fun activeOne(): Wallet? {
    return database.get().walletDao.activeWallet()?.toWallet()
  }
}
