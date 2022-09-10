package com.crypto.defi.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.WorkManager
import com.crypto.defi.chains.ChainRepository
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
                        Log.d("Http", message)
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
    fun provideChainRepository(
        database: CryptoDeFiDatabase,
        client: HttpClient
    ): ChainRepository {
        return ChainRepository(
            database = database,
            client = client
        )
    }
}