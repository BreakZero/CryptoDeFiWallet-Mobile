package com.crypto.defi.models.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_chain")
data class ChainEntity(
  @PrimaryKey
  val code: String,
  @ColumnInfo(name = "chain_type")
  val chainType: String,
  @ColumnInfo(name = "chain_id")
  val chainId: String? = null,
  @ColumnInfo(name = "is_test_net")
  val isTestNet: Boolean,
  val name: String,
  @ColumnInfo(name = "is_token")
  val isToken: Boolean
)
