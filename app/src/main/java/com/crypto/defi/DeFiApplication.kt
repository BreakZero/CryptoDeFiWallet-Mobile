package com.crypto.defi

import android.app.Application
import androidx.work.Configuration
import com.crypto.defi.workers.DeFiWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DeFiApplication: Application(), Configuration.Provider {
    init {
        System.loadLibrary("TrustWalletCore")
    }

    @Inject
    lateinit var workerFactory: DeFiWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}