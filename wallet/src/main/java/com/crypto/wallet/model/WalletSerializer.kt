package com.crypto.wallet.model

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@kotlinx.serialization.Serializable
data class Wallet(
    val mnemonic: String = "",
    val active: Int = 0, // 0 = inactive, 1 = active
    val passphrase: String = ""
)

@ExperimentalSerializationApi
object WalletSerializer: Serializer<Wallet> {
    override val defaultValue: Wallet
        get() = Wallet()

    override suspend fun readFrom(input: InputStream): Wallet {
        return try {
            Json.decodeFromString(
                deserializer = Wallet.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: Wallet, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = Wallet.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}