package com.crypto.defi.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.chains.usecase.AssetUseCase
import com.crypto.defi.chains.usecase.BalanceUseCase
import com.crypto.defi.models.domain.AppSettingsConfig
import com.crypto.defi.models.domain.AppSettingsConfigSerializer
import com.crypto.defi.models.local.CryptoDeFiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val SETTINS_PREFERENCES = "app_settings"

@Module
@InstallIn(SingletonComponent::class)
object SingleModule {

  @Provides
  @Singleton
  fun provideKtorClient(): HttpClient {
    return HttpClient(
      Android.config {
        connectTimeout = 10_000
      /*sslManager = { httpsURLConnection ->
        httpsURLConnection.sslSocketFactory = SslSettings.getSslContext(application.applicationContext)?.socketFactory
      }*/
      },
    ) {
      defaultRequest {
        header("Content-type", "application/json")
//        header("network", "ropsten")
        contentType(ContentType.Application.Json)
      }
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            useArrayPolymorphism = true
            prettyPrint = true
            allowStructuredMapKeys = true
          },
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
    }
  }

  @Provides
  @Singleton
  fun provideDeFiDatabase(
    application: Application,
  ): CryptoDeFiDatabase {
    return Room.databaseBuilder(application, CryptoDeFiDatabase::class.java, "defi_wallet.db")
      .addMigrations(CryptoDeFiDatabase.Migrations.MIGRATION_2_3)
      .build()
  }

  @Provides
  @Singleton
  fun provideChainManager(
    database: CryptoDeFiDatabase,
    client: HttpClient,
  ): ChainManager {
    return ChainManager(
      database = database,
      client = client,
    )
  }

  @Provides
  @Singleton
  fun provideBalanceUseCase(
    database: CryptoDeFiDatabase,
    chainManager: ChainManager,
    client: HttpClient,
  ): BalanceUseCase {
    return BalanceUseCase(
      client = client,
      chainManager = chainManager,
      database = database,
    )
  }

  @Provides
  @Singleton
  fun provideAssetUseCase(
    client: HttpClient,
    database: CryptoDeFiDatabase,
  ): AssetUseCase {
    return AssetUseCase(client = client, database = database)
  }

  @Singleton
  @Provides
  fun provideAppSettingsDataStore(@ApplicationContext appContext: Context): DataStore<AppSettingsConfig> {
    return DataStoreFactory.create(
      serializer = AppSettingsConfigSerializer,
      produceFile = { appContext.dataStoreFile(SETTINS_PREFERENCES) },
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
      corruptionHandler = null,
    )
  }
}
