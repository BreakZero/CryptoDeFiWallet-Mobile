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

package com.easy.defi.app.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetDto(
  @SerialName("currencies")
  val currencies: List<CurrencyDto>,
  @SerialName("sha256")
  val sha256: String,
)

@Serializable
data class CurrencyDto(
  @SerialName("chain")
  val chain: String,
  @SerialName("chainName")
  val chainName: String,
  @SerialName("coinId")
  val coinId: String,
  @SerialName("coinName")
  val coinName: String,
  @SerialName("contractAddress")
  val contractAddress: String,
  @SerialName("iconUrl")
  val iconUrl: String,
  @SerialName("isErc20")
  val isErc20: Boolean,
  @SerialName("network")
  val network: String,
  @SerialName("originCoinId")
  val originCoinId: String,
  @SerialName("parent")
  val parent: String,
  @SerialName("slug")
  val slug: String,
  @SerialName("symbol")
  val symbol: String,
  @SerialName("tokenDecimal")
  val tokenDecimal: Int,
)
