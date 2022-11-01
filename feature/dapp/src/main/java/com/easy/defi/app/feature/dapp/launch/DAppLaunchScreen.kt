package com.easy.defi.app.feature.dapp.launch

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.defi.app.core.common.extensions.launchWithHandler
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.feature.dapp.R
import com.easy.defi.app.feature.dapp.launch.util.ActionMethod
import com.easy.defi.app.feature.dapp.launch.util.WebInterface
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.Dispatchers

@Composable
internal fun DAppLaunchScreen(
  dAppLaunchViewModel: DAppLaunchViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val provideJs by remember {
    mutableStateOf(
      context.resources.openRawResource(R.raw.trust_min).bufferedReader().use {
        it.readText()
      }
    )
  }

  val navigator = rememberWebViewNavigator()
  Column(modifier = Modifier.fillMaxSize()) {
    DeFiAppBar {
      if (navigator.canGoBack) {
        navigator.navigateBack()
      } else {
        onBackClick()
      }
    }
    val webViewState = rememberWebViewState(url = dAppLaunchViewModel.webUrl)
    val loadingState = webViewState.loadingState
    val webClient = remember {
      object : AccompanistWebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
          super.onPageStarted(view, url, favicon)
          view?.also { webView ->
            webView.evaluateJavascript(provideJs, null)
            webView.evaluateJavascript(dAppLaunchViewModel.initJs, null)
            dAppLaunchViewModel.requestAccount(webView)
          }
        }
      }
    }

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
      client = webClient,
      onCreated = { webView ->
        webView.settings.run {
          javaScriptEnabled = true
          domStorageEnabled = true
          databaseEnabled = true
          useWideViewPort = true
          loadWithOverviewMode = true
        }
        webView.addJavascriptInterface(
          WebInterface { message ->
            dAppLaunchViewModel.updateMessage(message)
            scope.launchWithHandler(Dispatchers.Main) {
              when (message.method) {
                ActionMethod.REQUESTACCOUNTS -> {
                  dAppLaunchViewModel.setAddress(webView)
                  dAppLaunchViewModel.sendAddress(webView, message.methodId)
                }
                ActionMethod.SWITCHETHEREUMCHAIN -> {
                }
                else -> Unit
              }
            }
          },
          "_tw_"
        )
      }
    )
  }
}
