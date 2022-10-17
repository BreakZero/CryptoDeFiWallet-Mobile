package com.crypto.defi.models.domain

import kotlinx.serialization.Serializable
import androidx.datastore.core.Serializer
import com.crypto.defi.common.DeFiConstant
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.*

val SUPPORT_NETWORKS = listOf(
  ChainNetwork.MAINNET, ChainNetwork.ROPSTEN, ChainNetwork.RINKEBY
)

enum class ChainNetwork(
  val label: String
) {
  MAINNET("MainNet"), ROPSTEN("Ropsten"), RINKEBY("Rinkeby")
}

@Serializable
data class DeFiCurrency(
  val symbol: String,
  val code: String
)

@Serializable
data class AppSettingsConfig(
  val currency: DeFiCurrency,
  val network: ChainNetwork,
  val walletNameInfo: WalletNameInfo
)

@Serializable
data class WalletNameInfo(
  val avatorRes: Int,
  val avator: String?,
  val walletName: String
) {
  companion object {
    val Default = WalletNameInfo(
      avatorRes = com.crypto.resource.R.drawable.avatar_generic_1,
      avator = null,
      walletName = DeFiConstant.DEFAULT_WALLET_NAME
    )
  }
}

object AppSettingsConfigSerializer : Serializer<AppSettingsConfig> {
  override val defaultValue: AppSettingsConfig
    get() = AppSettingsConfig(
      network = ChainNetwork.MAINNET,
      currency = Currency.getInstance(Locale.US).let {
        DeFiCurrency(it.symbol, it.currencyCode)
      },
      walletNameInfo = WalletNameInfo(
        avatorRes = com.crypto.resource.R.drawable.avatar_generic_1,
        avator = null,
        walletName = DeFiConstant.DEFAULT_WALLET_NAME
      )
    )

  override suspend fun readFrom(input: InputStream): AppSettingsConfig {
    return try {
      Json.decodeFromString(
        deserializer = AppSettingsConfig.serializer(),
        string = input.readBytes().decodeToString()
      )
    } catch (e: Exception) {
      e.printStackTrace()
      defaultValue
    }
  }

  override suspend fun writeTo(t: AppSettingsConfig, output: OutputStream) {
    output.write(
      Json.encodeToString(
        serializer = AppSettingsConfig.serializer(),
        value = t
      ).encodeToByteArray()
    )
  }
}
