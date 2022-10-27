package com.easy.defi.app.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_asset")
data class AssetEntity(
  @PrimaryKey
  val slug: String,
  @ColumnInfo(name = "code")
  val code: String,
  @ColumnInfo(name = "icon_url")
  val iconUrl: String,
  @ColumnInfo(name = "name")
  val name: String,
  @ColumnInfo(name = "symbol")
  val symbol: String,
  @ColumnInfo(name = "decimal")
  val decimal: Int,
  @ColumnInfo(name = "chain_name")
  val chainName: String,
  @ColumnInfo(name = "contract_address")
  val contractAddress: String,
  @ColumnInfo(name = "balance")
  val balance: String,
)
