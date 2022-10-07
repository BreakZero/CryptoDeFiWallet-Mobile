package com.crypto.defi.models.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_version_sha256")
data class CoinVersionShaEntity(
  @PrimaryKey
  @ColumnInfo(name = "id")
  val sha256: String,
  @ColumnInfo(name = "create_at")
  val createAt: Long
)
