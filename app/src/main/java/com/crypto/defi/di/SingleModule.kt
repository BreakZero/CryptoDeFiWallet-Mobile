package com.crypto.defi.di

import android.app.Application
import androidx.room.Room
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import com.crypto.defi.chains.usecase.BalanceUseCase
import com.crypto.defi.models.local.CryptoDeFiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingleModule {

  @Provides
  @Singleton
  fun provideKtorClient(): HttpClient {
    return HttpClient(Android) {
      defaultRequest {
        header("Content-type", "application/json")
        header("network", "ropsten")
        contentType(ContentType.Application.Json)
      }
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            useArrayPolymorphism = true
            prettyPrint = true
            allowStructuredMapKeys = true
          }
        )
      }
      install(Logging) {
        logger = object : Logger {
          override fun log(message: String) {
            Timber.v(message)
          }
        }
        level = LogLevel.BODY
      }
      engine {
        connectTimeout = 10_000
      }
    }
  }

  @Provides
  @Singleton
  fun provideDeFiDatabase(
    application: Application
  ): CryptoDeFiDatabase {
    return Room.databaseBuilder(application, CryptoDeFiDatabase::class.java, "defi_wallet.db")
      .build()
  }

  @Provides
  @Singleton
  fun provideChainManager(
    database: CryptoDeFiDatabase,
    client: HttpClient
  ): ChainManager {
    return ChainManager(
      database = database,
      client = client
    )
  }

  @Provides
  @Singleton
  fun provideBalanceUseCase(
    database: CryptoDeFiDatabase,
    client: HttpClient
  ): BalanceUseCase {
    return BalanceUseCase(
      client = client,
      database = database
    )
  }

  @Provides
  @Singleton
  fun provideAssetUseCase(
    client: HttpClient,
    database: CryptoDeFiDatabase
  ): AssetUseCase {
    return AssetUseCase(client = client, database = database)
  }
}