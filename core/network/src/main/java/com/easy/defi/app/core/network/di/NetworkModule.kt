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
      }
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
          }
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
