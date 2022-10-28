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

package com.easy.defi.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.easy.defi.app.core.database.model.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWallet(entity: WalletEntity)

  @Delete
  suspend fun deleteWallet(entity: WalletEntity)

  @Query(
    """
    SELECT *
    FROM TB_WALLET
    WHERE active = 1
    """
  )
  fun activeWallet(): Flow<WalletEntity?>
}
