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

package com.easy.defi.app.core.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

class ConnectivityManagerNetworkMonitor @Inject constructor(
  @ApplicationContext private val context: Context,
) : NetworkMonitor {
  override val isOnline: Flow<Boolean> = callbackFlow<Boolean> {
    val callback = object : NetworkCallback() {
      override fun onAvailable(network: Network) {
        channel.trySend(true)
      }

      override fun onLost(network: Network) {
        channel.trySend(false)
      }
    }

    val connectivityManager = context.getSystemService<ConnectivityManager>()

    connectivityManager?.registerNetworkCallback(
      Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build(),
      callback,
    )

    channel.trySend(connectivityManager.isCurrentlyConnected())

    awaitClose {
      connectivityManager?.unregisterNetworkCallback(callback)
    }
  }
    .conflate()

  @Suppress("DEPRECATION")
  private fun ConnectivityManager?.isCurrentlyConnected() = when (this) {
    null -> false
    else -> when {
      VERSION.SDK_INT >= VERSION_CODES.M ->
        activeNetwork
          ?.let(::getNetworkCapabilities)
          ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
          ?: false
      else -> activeNetworkInfo?.isConnected ?: false
    }
  }
}
