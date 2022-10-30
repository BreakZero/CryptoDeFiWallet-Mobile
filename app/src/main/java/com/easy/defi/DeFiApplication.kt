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

package com.easy.defi

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.easy.defi.app.sync.work.initializers.Sync
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DeFiApplication : Application(), ImageLoaderFactory {
  init {
    System.loadLibrary("TrustWalletCore")
  }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Sync.initialize(context = this)
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
