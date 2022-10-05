package com.crypto.defi

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.crypto.defi.workers.DeFiWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.*
import javax.inject.Inject


@HiltAndroidApp
class DeFiApplication : Application(), Configuration.Provider, ImageLoaderFactory {
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

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                // GIFs
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.memoryCache {
                MemoryCache.Builder(this)
                    // Set the max size to 25% of the app's available memory.
                    .maxSizePercent(0.25)
                    .build()
            }.diskCache {
                DiskCache.Builder()
                    .directory(filesDir.resolve("image_cache"))
                    .maxSizeBytes(512L * 1024 * 1024) // 512MB
                    .build()
            }.build()
    }
}

private class CrashReportingTree : Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority in listOf(Log.VERBOSE, Log.DEBUG)) {
            return;
        }
        // push information to firebase or some cloud service else
    }
}