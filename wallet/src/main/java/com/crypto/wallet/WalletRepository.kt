package com.crypto.wallet

import com.crypto.wallet.model.WalletDatabase
import com.crypto.wallet.model.WalletEntity
import wallet.core.jni.HDWallet
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val database: dagger.Lazy<WalletDatabase>
) {
    lateinit var hdWallet: HDWallet
        private set

    suspend fun insertWallet(wallet: WalletEntity) {
        database.get().walletDao.insertWallet(wallet)
    }

    suspend fun deleteOne(wallet: WalletEntity) {
        database.get().walletDao.deleteWallet(wallet)
    }

    suspend fun activeOne(): WalletEntity? {
        return database.get().walletDao.activeWallet()
    }

    fun inject(hdWallet: HDWallet) {
        this.hdWallet = hdWallet
    }
}
