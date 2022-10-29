package com.easy.defi.app.core.data

import wallet.core.jni.HDWallet
import javax.inject.Inject

class HdWalletHolder @Inject constructor() {
  var hdWallet: HDWallet? = null
    private set

  fun inject(mnemonic: String, passphrase: String) {
    hdWallet?.let {
      if (it.mnemonic() == mnemonic) return@let
      hdWallet = HDWallet(mnemonic, passphrase)
    } ?: kotlin.run {
      hdWallet = HDWallet(mnemonic, passphrase)
    }
  }
}
