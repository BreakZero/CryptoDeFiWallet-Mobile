package com.easy.defi.app.core.domain

import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.model.Wallet
import javax.inject.Inject
import wallet.core.jni.Mnemonic

class InsertWalletUseCase @Inject constructor(
  private val walletRepository: WalletRepository,
) {
  suspend operator fun invoke(mnemonic: String, passphrase: String = "", onResult: suspend (Boolean) -> Unit) {
    val isValid = Mnemonic.isValid(mnemonic)
    if (isValid) {
      walletRepository.insertWallet(
        Wallet(
          mnemonic = mnemonic,
          active = 1,
          passphrase = passphrase,
        ),
      )
    }
    onResult(isValid)
  }
}
