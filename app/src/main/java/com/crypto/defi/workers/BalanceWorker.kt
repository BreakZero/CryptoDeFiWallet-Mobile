package com.crypto.defi.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.crypto.defi.models.local.CryptoDeFiDatabase
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BalanceWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    val database: CryptoDeFiDatabase,
    val client: HttpClient
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            client.get(
                "http://192.168.1.105:8080/ethereum/balance/0x81080a7e991bcdddba8c2302a70f45d6bd369ab5"
            ).bodyAsText().also {
                Log.d("=====", it)
            }
        }
        return Result.success()
    }
}