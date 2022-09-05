package com.crypto.defi

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DeFiApplication: Application() {
    init {
        System.loadLibrary("TrustWalletCore")
    }
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}