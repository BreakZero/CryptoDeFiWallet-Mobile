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

package com.easy.defi.app.core.data.repository

import com.easy.defi.app.core.data.model.asEntity
import com.easy.defi.app.core.database.WalletDatabase
import com.easy.defi.app.core.database.model.asExternalModel
import com.easy.defi.app.core.model.data.Wallet
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class WalletRepository @Inject constructor(
  private val database: dagger.Lazy<WalletDatabase>,
) {
  suspend fun insertWallet(wallet: Wallet) {
    database.get().walletDao.insertWallet(wallet.asEntity())
  }

  suspend fun deleteOne(wallet: Wallet) {
    database.get().walletDao.deleteWallet(wallet.asEntity())
  }

  fun activeWalletStream(): Flow<Wallet> {
    return database.get().walletDao.activeWallet().filterNotNull().map {
      it.asExternalModel()
    }
  }
}
