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

package com.easy.defi.app.core.model.data

import java.math.BigInteger

open class BaseTransaction(
  val hash: String,
  val direction: TransactionDirection,
  val from: String,
  val to: String,
  val value: BigInteger,
  val timeStamp: String,
)

enum class TransactionDirection {
  SEND, RECEIVE, SEND_SELF
}
