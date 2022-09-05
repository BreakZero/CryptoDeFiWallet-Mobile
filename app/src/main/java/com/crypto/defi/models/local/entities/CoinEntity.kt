package com.crypto.defi.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_coin")
data class CoinEntity(
    @PrimaryKey
    val slug: String
)
