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
import com.easy.defi.app.core.model.data.Chain

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

fun ChainEntity.asExternalModel(): Chain {
  return Chain(
    code = code,
    chainType = chainType,
    chainId = chainId,
    isTestNet = isTestNet,
    name = name,
    isToken = isToken
  )
}
