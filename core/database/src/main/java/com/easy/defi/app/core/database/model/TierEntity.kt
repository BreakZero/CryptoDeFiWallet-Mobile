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
import com.easy.defi.app.core.model.data.Tier

@Entity(tableName = "tb_tier", primaryKeys = ["from_currency", "to_currency"])
data class TierEntity(
  @ColumnInfo(name = "from_currency")
  val fromCurrency: String,
  @ColumnInfo(name = "from_slug", defaultValue = "--")
  val fromSlug: String,
  @ColumnInfo(name = "to_currency")
  val toCurrency: String,
  @ColumnInfo(name = "rate", defaultValue = "0.0")
  val rate: String,
  @ColumnInfo(name = "time_stamp")
  val timeStamp: String,
)

fun TierEntity.asExternalModel(): Tier {
  return Tier(
    fromCurrency = fromCurrency,
    fromSlug = fromSlug,
    toCurrency = toCurrency,
    rate = rate
  )
}
