package com.crypto.defi.models.mapper

import com.crypto.core.extensions.byDecimal
import com.crypto.defi.models.domain.Asset
import com.crypto.defi.models.local.entities.AssetEntity
import com.crypto.defi.models.remote.Currency

fun AssetEntity.toAsset(): Asset {
    return Asset(
        slug = this.slug,
        code = this.code,
        iconUrl = this.iconUrl,
        name = this.name,
        symbol = this.symbol,
        decimal = this.decimal,
        chainName = this.chainName,
        contract = this.contractAddress,
        nativeBalance = this.balance.toBigInteger().byDecimal(this.decimal)
    )
}

internal fun Currency.toAssetEntity(): AssetEntity {
    return AssetEntity(
        slug = this.slug,
        code = this.chain,
        iconUrl = this.iconUrl,
        name = this.coinName,
        symbol = this.symbol,
        decimal = this.tokenDecimal,
        chainName = this.chainName,
        contractAddress = this.contractAddress,
        balance = "0"
    )
}