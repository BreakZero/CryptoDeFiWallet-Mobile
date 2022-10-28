package com.easy.defi.app.core.domain

import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.model.data.Wallet
import javax.inject.Inject
import wallet.core.jni.Mnemonic

class InsertWalletUseCase @Inject constructor(
  private val walletRepository: WalletRepository,
) {
  suspend operator fun invoke(
    mnemonic: String,
    passphrase: String = "",
    doFirst: suspend () -> Unit,
    doLast: suspend (Boolean) -> Unit,
  ) {
    val isValid = Mnemonic.isValid(mnemonic)
    if (isValid) {
      doFirst()
      walletRepository.insertWallet(
        Wallet(
          mnemonic = mnemonic,
          active = 1,
          passphrase = passphrase,
        ),
      )
    }
    doLast(isValid)
  }
}
