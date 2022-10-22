@file:OptIn(ExperimentalMaterial3Api::class)

package com.crypto.defi.feature.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.ScannerView
import com.crypto.core.ui.utils.SetStatusColor
import com.crypto.defi.BuildConfig
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeFiWebViewScreen(
  url: String,
  popBack: () -> Unit
) {
  val navigator = rememberWebViewNavigator()
  Scaffold(
    topBar = {
      DeFiAppBar {
        if (navigator.canGoBack) {
          navigator.navigateBack()
        } else {
          popBack()
        }
      }
    }
  ) { paddings ->
    val webUrl by remember {
      mutableStateOf(url)
    }
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddings)
    ) {
      val webViewState = rememberWebViewState(url = webUrl)
      val loadingState = webViewState.loadingState
      if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
          color = MaterialTheme.colorScheme.tertiary,
          progress = loadingState.progress,
          modifier = Modifier.fillMaxWidth()
        )
      }
      WebView(
        state = webViewState,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        navigator = navigator,
        onCreated = { webView ->
          webView.settings.run {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            userAgentString = "$userAgentString DeFiWallet/${BuildConfig.VERSION_NAME}"
          }
        }
      )
    }
  }
}