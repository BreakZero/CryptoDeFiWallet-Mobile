package com.crypto.defi.models.remote.nft


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NftAssetGroup(
    @SerialName("assets")
    val assets: List<NftInfo>,
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
data class NftOwnerAssets(
    @SerialName("content")
    val content: List<NftInfo>,
    @SerialName("next")
    val next: String,
    @SerialName("total")
    val total: Int
)

@Serializable
data class NftAttribute(
    @SerialName("attribute_name")
    val attributeName: String,
    @SerialName("attribute_value")
    val attributeValue: String,
    @SerialName("percentage")
    val percentage: String
)

@Serializable
data class NftInfo(
    @SerialName("amount")
    val amount: String,
    @SerialName("attributes")
    val attributes: List<NftAttribute>?,
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