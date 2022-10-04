package com.crypto.defi

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.crypto.defi.workers.DeFiWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.*
import javax.inject.Inject


@HiltAndroidApp
class DeFiApplication : Application(), Configuration.Provider {
    init {
        System.loadLibrary("TrustWalletCore")
    }

    @Inject
    lateinit var workerFactory: DeFiWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO)
            .build()
}

private class CrashReportingTree : Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority in listOf(Log.VERBOSE, Log.DEBUG)) {
            return;
        }
        // push information to firebase or some cloud service else
    }
}