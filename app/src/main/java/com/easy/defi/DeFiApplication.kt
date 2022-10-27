package com.easy.defi

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DeFiApplication : Application(), ImageLoaderFactory {
  override fun onCreate() {
    super.onCreate()
  }

  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(this)
      .components {
        add(SvgDecoder.Factory())
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
