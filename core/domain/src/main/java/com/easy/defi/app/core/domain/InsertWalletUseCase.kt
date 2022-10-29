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

package com.easy.defi.app.core.domain

import com.easy.defi.app.core.data.repository.WalletRepository
import com.easy.defi.app.core.model.data.Wallet
import wallet.core.jni.Mnemonic
import javax.inject.Inject

class InsertWalletUseCase @Inject constructor(
  private val walletRepository: WalletRepository
) {
  suspend operator fun invoke(
    mnemonic: String,
    passphrase: String = "",
    doFirst: suspend () -> Unit,
    doLast: suspend (Boolean) -> Unit
  ) {
    val isValid = Mnemonic.isValid(mnemonic)
    if (isValid) {
      doFirst()
      walletRepository.insertWallet(
        Wallet(
          mnemonic = mnemonic,
          active = 1,
          passphrase = passphrase
        )
      )
    }
    doLast(isValid)
  }
}
