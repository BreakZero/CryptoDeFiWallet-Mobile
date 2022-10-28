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

package com.easy.defi.app.onboarding.passcode

import androidx.annotation.Keep

@Keep
data class PasscodeState(
  val passcode: String = "",
  val originPasscode: String = "",
  val messageLabel: String,
  val error: String? = null,
) {
  fun insert(number: String): PasscodeState {
    return PasscodeState(
      passcode = "$passcode$number",
      messageLabel = messageLabel,
      originPasscode = originPasscode
    )
  }

  fun delete(): PasscodeState {
    return PasscodeState(
      passcode = passcode.dropLast(1),
      messageLabel = messageLabel,
      originPasscode = originPasscode
    )
  }

  fun clear(messageLabel: String, error: String): PasscodeState {
    return PasscodeState(
      passcode = "",
      originPasscode = "",
      messageLabel = messageLabel,
      error = error
    )
  }

  fun originDone(messageLabel: String): PasscodeState {
    val originPasscode = passcode
    return PasscodeState(
      passcode = "",
      originPasscode = originPasscode,
      messageLabel = messageLabel,
      error = null
    )
  }
}
