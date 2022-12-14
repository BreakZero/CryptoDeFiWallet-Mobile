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

package com.easy.defi.app.core.ui.utils

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.core.app.ActivityCompat

object BiometricUtil {
  fun launchBiometric(
    context: Context,
    title: String = "",
    subtitle: String = "",
    description: String = "",
    authenticationCallback: BiometricPrompt.AuthenticationCallback,
    onCancel: () -> Unit
  ) {
    if (checkBiometricSupport(context)) {
      val biometricPrompt = BiometricPrompt.Builder(context)
        .apply {
          setTitle(title)
          setSubtitle(subtitle)
          setDescription(description)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setConfirmationRequired(false)
          }
          setNegativeButton(
            "Using App Password",
            context.mainExecutor
          ) { _, _ ->
            onCancel.invoke()
          }
        }.build()

      biometricPrompt.authenticate(
        CancellationSignal()
          .apply {
            setOnCancelListener {
              onCancel.invoke()
            }
          },
        context.mainExecutor,
        authenticationCallback
      )
    }
  }

  private fun checkBiometricSupport(context: Context): Boolean {
    val keyGuardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    if (!keyGuardManager.isDeviceSecure) {
      return true
    }
    if (ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.USE_BIOMETRIC
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      return false
    }

    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
  }
}
