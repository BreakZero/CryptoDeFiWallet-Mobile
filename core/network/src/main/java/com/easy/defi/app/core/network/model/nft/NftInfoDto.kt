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

package com.easy.defi.app.core.network.model.nft

import com.easy.defi.app.core.model.data.NftAttribute
import com.easy.defi.app.core.model.data.NftGroup
import com.easy.defi.app.core.model.data.NftInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NftAssetGroupDto(
  @SerialName("assets")
  val assets: List<NftInfoDto>,
  @SerialName("contract_address")
  val contractAddress: String,
  @SerialName("contract_name")
  val contractName: String,
  @SerialName("description")
  val description: String?,
  @SerialName("floor_price")
  val floorPrice: Double?,
  @SerialName("items_total")
  val itemsTotal: Int,
  @SerialName("logo_url")
  val logoUrl: String?,
  @SerialName("owns_total")
  val ownsTotal: Int
)

@Serializable
internal data class NftOwnerAssetsDto(
  @SerialName("content")
  val content: List<NftInfoDto>,
  @SerialName("next")
  val next: String,
  @SerialName("total")
  val total: Int
)

@Serializable
internal data class NftAttributeDto(
  @SerialName("attribute_name")
  val attributeName: String,
  @SerialName("attribute_value")
  val attributeValue: String,
  @SerialName("percentage")
  val percentage: String?
)

@Serializable
internal data class NftInfoDto(
  @SerialName("amount")
  val amount: String,
  @SerialName("attributes")
  val attributes: List<NftAttributeDto>?,
  @SerialName("content_type")
  val contentType: String?,
  @SerialName("content_uri")
  val contentUri: String?,
  @SerialName("contract_address")
  val contractAddress: String,
  @SerialName("contract_name")
  val contractName: String,
  @SerialName("contract_token_id")
  val contractTokenId: String,
  @SerialName("erc_type")
  val ercType: String,
  @SerialName("external_link")
  val externalLink: String?,
  @SerialName("image_uri")
  val imageUri: String?,
  @SerialName("latest_trade_price")
  val latestTradePrice: Double?,
  @SerialName("latest_trade_symbol")
  val latestTradeSymbol: String?,
  @SerialName("latest_trade_timestamp")
  val latestTradeTimestamp: Long?,
  @SerialName("metadata_json")
  val metadataJson: String?,
  @SerialName("mint_price")
  val mintPrice: Double?,
  @SerialName("mint_timestamp")
  val mintTimestamp: Long?,
  @SerialName("mint_transaction_hash")
  val mintTransactionHash: String,
  @SerialName("minter")
  val minter: String,
  @SerialName("name")
  val name: String?,
  @SerialName("nftscan_id")
  val nftscanId: String?,
  @SerialName("nftscan_uri")
  val nftscanUri: String?,
  @SerialName("owner")
  val owner: String?,
  @SerialName("token_id")
  val tokenId: String,
  @SerialName("token_uri")
  val tokenUri: String?
)

internal fun NftAssetGroupDto.asExternalModel(): NftGroup {
  return NftGroup(
    assets = assets.map(NftInfoDto::asExternalModel),
    contractAddress = contractAddress,
    contractName = contractName,
    description = description,
    floorPrice = floorPrice,
    itemsTotal = itemsTotal,
    logoUrl = logoUrl,
    ownsTotal = ownsTotal
  )
}

internal fun NftAttributeDto.asExternalModel(): NftAttribute {
  return NftAttribute(
    attributeName = attributeName,
    attributeValue = attributeValue,
    percentage = percentage
  )
}

internal fun NftInfoDto.asExternalModel(): NftInfo {
  return NftInfo(
    amount = amount,
    attributes = attributes?.map(NftAttributeDto::asExternalModel),
    contentType = contentType,
    contentUri = contentUri,
    contractAddress = contractAddress,
    contractName = contractName,
    contractTokenId = contractTokenId,
    ercType = ercType,
    externalLink = externalLink,
    imageUri = imageUri,
    latestTradePrice = latestTradePrice,
    latestTradeSymbol = latestTradeSymbol,
    latestTradeTimestamp = latestTradeTimestamp,
    metadataJson = metadataJson,
    mintPrice = mintPrice,
    mintTimestamp = mintTimestamp,
    mintTransactionHash = mintTransactionHash,
    minter = minter,
    name = name,
    nftscanId = nftscanId,
    nftscanUri = nftscanUri,
    owner = owner,
    tokenId = tokenId,
    tokenUri = tokenUri
  )
}
