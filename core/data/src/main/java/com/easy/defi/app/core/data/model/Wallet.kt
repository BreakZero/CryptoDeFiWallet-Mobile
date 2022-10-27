package com.easy.defi.app.core.data.model

import com.easy.defi.app.core.database.model.WalletEntity
import com.easy.defi.app.core.model.data.Wallet

fun Wallet.asEntity() = WalletEntity(
  mnemonic = mnemonic,
  active = active,
  passphrase = passphrase,
)
