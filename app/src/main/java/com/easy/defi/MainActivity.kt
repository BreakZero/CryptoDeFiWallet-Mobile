package com.easy.defi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.easy.defi.app.core.data.util.NetworkMonitor
import com.easy.defi.app.core.designsystem.theme.DeFiWalletTheme
import com.easy.defi.ui.DeFiApp
import com.easy.defi.ui.rememberDeFiAppState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var networkMonitor: NetworkMonitor
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      val systemUiController = rememberSystemUiController()
      DeFiWalletTheme(
        darkTheme = false,
      ) {
        DeFiApp(
          networkMonitor = networkMonitor,
          appState = rememberDeFiAppState(
            networkMonitor = networkMonitor,
            windowSizeClass = calculateWindowSizeClass(this),
          ),
        )
      }
    }
  }
}
