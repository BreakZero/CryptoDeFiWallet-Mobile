package com.crypto.defi.models.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AssetDto(
  @SerialName("currencies")
  val currencies: List<Currency>,
  @SerialName("sha256")
  val sha256: String
)


@Serializable
internal data class Currency(
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
  val tokenDecimal: Int
)