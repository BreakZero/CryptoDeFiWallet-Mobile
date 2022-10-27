package com.easy.defi.app.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.easy.defi.app.core.model.Wallet

@Entity(
  tableName = "tb_wallet",
)
data class WalletEntity(
  @PrimaryKey val mnemonic: String,
  val active: Int, // 0 = inactive, 1 = active
  val passphrase: String,
)

fun WalletEntity.asExternalModel() = Wallet(
  mnemonic = mnemonic,
  active = active,
  passphrase = passphrase,
)
