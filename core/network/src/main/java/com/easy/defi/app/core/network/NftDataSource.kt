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

import com.easy.defi.app.core.model.data.NftGroup
import com.easy.defi.app.core.model.data.NftInfo
import com.easy.defi.app.core.network.model.nft.BaseNftResponse
import com.easy.defi.app.core.network.model.nft.NftAssetGroupDto
import com.easy.defi.app.core.network.model.nft.NftInfoDto
import com.easy.defi.app.core.network.model.nft.asExternalModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class NftDataSource @Inject constructor(
  private val httpClient: HttpClient
) {

  companion object {
    private const val API_KEY = ""
  }
  suspend fun groupOfType(address: String, ercType: String): List<NftGroup> {
    val response: BaseNftResponse<List<NftAssetGroupDto>> = httpClient.get {
      url("${UrlConstant.NFT_SCAN_URL}/account/own/all/$address")
      header("X-API-KEY", API_KEY)
      parameter("erc_type", ercType)
      parameter("show_attribute", false)
    }.body()
    return response.data.map(NftAssetGroupDto::asExternalModel)
  }

  suspend fun getNftAssetByTokenId(contractAddress: String, tokenId: String): NftInfo {
    val response: BaseNftResponse<NftInfoDto> = httpClient.get {
      url("${UrlConstant.NFT_SCAN_URL}/assets/$contractAddress/$tokenId")
      header("X-API-KEY", API_KEY)
      parameter("show_attribute", true)
    }.body()
    return response.data.asExternalModel()
  }
}
