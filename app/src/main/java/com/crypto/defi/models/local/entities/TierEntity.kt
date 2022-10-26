package com.crypto.defi.models.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "tb_tier", primaryKeys = ["from_currency", "to_currency"])
data class TierEntity(
  @ColumnInfo(name = "from_currency")
  val fromCurrency: String,
  @ColumnInfo(name = "from_slug", defaultValue = "--")
  val fromSlug: String,
  @ColumnInfo(name = "to_currency")
  val toCurrency: String,
  val rate: String,
  @ColumnInfo(name = "time_stamp")
  val timeStamp: String,
)
