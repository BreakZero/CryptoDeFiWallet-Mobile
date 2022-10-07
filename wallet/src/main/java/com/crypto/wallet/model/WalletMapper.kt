package com.crypto.wallet.model

internal fun WalletEntity.toWallet(): Wallet {
  return Wallet(
      mnemonic = this.mnemonic,
      active = this.active,
      passphrase = this.passphrase
  )
}

internal fun Wallet.toWalletEntity(): WalletEntity {
  return WalletEntity(
      mnemonic = this.mnemonic,
      active = this.active,
      passphrase = this.passphrase
  )
}