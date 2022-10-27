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
    onCancel: () -> Unit,
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
            context.mainExecutor,
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
        authenticationCallback,
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
        Manifest.permission.USE_BIOMETRIC,
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      return false
    }

    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
  }
}
