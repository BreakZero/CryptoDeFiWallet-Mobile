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

package com.easy.defi.app.core.network

import com.easy.defi.app.core.network.model.AssetDto
import com.easy.defi.app.core.network.model.BaseResponse
import com.easy.defi.app.core.network.model.ChainDto
import com.easy.defi.app.core.network.model.TierDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class AssetDataSource @Inject constructor(
  private val httpClient: HttpClient,
) {
  suspend fun getChains(): List<String> {
    return try {
      httpClient.get("${UrlConstant.BASE_URL}/chains")
        .body<BaseResponse<List<ChainDto>>>().data.map {
          it.chainId.orEmpty()
        }
    } catch (e: Exception) {
      emptyList()
    }
  }

  suspend fun getCurrencies(
    lastSha256: String,
  ): AssetDto {
    return try {
      httpClient.get("${UrlConstant.BASE_URL}/currencies") {
        parameter("sha256", lastSha256)
      }.body<BaseResponse<AssetDto>>().data
    } catch (e: Exception) {
      AssetDto(emptyList(), "")
    }
  }

  suspend fun getTiers(): List<TierDto> {
    return try {
      httpClient.get("${UrlConstant.BASE_URL}/tiers")
        .body<BaseResponse<List<TierDto>>>().data
    } catch (e: Exception) {
      emptyList()
    }
  }
}
