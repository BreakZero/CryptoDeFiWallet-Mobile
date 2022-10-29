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
internal data class ChainDto(
  @SerialName("chain_type")
  val chainType: String?,
  @SerialName("code")
  val code: String,
  @SerialName("display_token_name")
  val displayTokenName: String,
  @SerialName("is_testnet")
  val isTestnet: Boolean,
  @SerialName("name")
  val name: String,
  @SerialName("parent_chain")
  val parentChain: String,
  @SerialName("position")
  val position: Int,
  @SerialName("chain_id")
  val chainId: String?,
  @SerialName("is_token")
  val isToken: Boolean
)

@Serializable
internal data class Details(
  @SerialName("chain_id")
  val chainId: String?,
  @SerialName("description")
  val description: String,
  @SerialName("full_description")
  val fullDescription: String,
  @SerialName("icon_link")
  val iconLink: String
)
