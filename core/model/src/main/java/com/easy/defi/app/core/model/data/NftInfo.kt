package com.easy.defi.app.core.model.data

data class NftGroup(
  val assets: List<NftInfo>,
  val contractAddress: String,
  val contractName: String,
  val description: String?,
  val floorPrice: Double?,
  val itemsTotal: Int,
  val logoUrl: String?,
  val ownsTotal: Int
)

data class NftAttribute(
  val attributeName: String,
  val attributeValue: String,
  val percentage: String?
)

data class NftInfo(
  val amount: String,
  val attributes: List<NftAttribute>?,
  val contentType: String?,
  val contentUri: String?,
  val contractAddress: String,
  val contractName: String,
  val contractTokenId: String,
  val ercType: String,
  val externalLink: String?,
  val imageUri: String?,
  val latestTradePrice: Double?,
  val latestTradeSymbol: String?,
  val latestTradeTimestamp: Long?,
  val metadataJson: String?,
  val mintPrice: Double?,
  val mintTimestamp: Long?,
  val mintTransactionHash: String,
  val minter: String,
  val name: String?,
  val nftscanId: String?,
  val nftscanUri: String?,
  val owner: String?,
  val tokenId: String,
  val tokenUri: String?
)
