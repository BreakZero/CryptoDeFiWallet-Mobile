package com.easy.defi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.easy.defi.app.core.data.util.NetworkMonitor
import com.easy.defi.app.core.designsystem.theme.DeFiWalletTheme
import com.easy.defi.app.onboarding.OnBoardingNavigations
import com.easy.defi.feature.asset.navigation.assetGraphRoutePattern
import com.easy.defi.ui.DeFiApp
import com.easy.defi.ui.rememberDeFiAppState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var networkMonitor: NetworkMonitor

  val viewModel: MainActivityViewModel by viewModels()
  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)

    var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

    // Update the uiState
    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState
          .onEach {
            uiState = it
          }
          .collect()
      }
    }

    splashScreen.setKeepOnScreenCondition {
      when (uiState) {
        MainActivityUiState.Loading -> true
        is MainActivityUiState.Success -> false
      }
    }

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      val systemUiController = rememberSystemUiController()
      val darkTheme = isSystemInDarkTheme()

      DisposableEffect(systemUiController, darkTheme) {
        systemUiController.systemBarsDarkContentEnabled = !darkTheme
        onDispose {}
      }
      val startDestination = when {
        uiState is MainActivityUiState.Success && (uiState as MainActivityUiState.Success).userData.hasPasscode -> {
          assetGraphRoutePattern
        }
        else -> OnBoardingNavigations.Index.destination
      }

      DeFiWalletTheme(
        darkTheme = darkTheme,
      ) {
        DeFiApp(
          startDestination = startDestination,
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
