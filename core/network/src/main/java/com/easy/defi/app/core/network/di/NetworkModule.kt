package com.easy.defi.app.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
import kotlinx.serialization.json.Json
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
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
            Timber.tag("Http Request").v(message)
          }
        }
        level = LogLevel.BODY
      }
    }
  }
}
