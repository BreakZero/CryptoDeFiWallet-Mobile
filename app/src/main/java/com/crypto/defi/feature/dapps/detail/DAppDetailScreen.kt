package com.crypto.defi.feature.dapps.detail

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.defi.BuildConfig
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DAppDetailScreen(
  dAppUrl: String,
  dAppRpc: String,
  popBack: () -> Unit
) {
  Timber.tag("=====").v(dAppRpc)
  val url by remember {
    mutableStateOf(dAppUrl)
  }
  val navigator = rememberWebViewNavigator()
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      DeFiAppBar() {
        if (navigator.canGoBack) {
          navigator.navigateBack()
        } else {
          popBack()
        }
      }
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(it)
    ) {
      val webViewState = rememberWebViewState(url = url)
      val loadingState = webViewState.loadingState
      if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
          color = MaterialTheme.colorScheme.secondary,
          progress = loadingState.progress,
          modifier = Modifier.fillMaxWidth()
        )
      }
      val webClient = remember {
        object : AccompanistWebViewClient() {
          override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Timber.v("Page started loading for $url")
          }
        }
      }
      WebView(
        state = webViewState,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        navigator = navigator,
        client = webClient,
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