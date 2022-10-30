/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi.app.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.easy.defi.app.core.model.data.Asset

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
  val balance: String
)

fun AssetEntity.asExternalModel(): Asset {
  return Asset(
    slug = this.slug,
    code = this.code,
    iconUrl = this.iconUrl,
    name = this.name,
    symbol = this.symbol,
    decimal = this.decimal,
    chainName = this.chainName,
    contract = this.contractAddress.ifBlank { null },
    nativeBalance = this.balance.toBigInteger()
  )
}
