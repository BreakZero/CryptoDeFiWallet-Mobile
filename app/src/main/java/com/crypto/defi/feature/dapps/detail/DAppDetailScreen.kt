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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.extensions.launchWithHandler
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.defi.BuildConfig
import com.crypto.defi.R
import com.crypto.defi.feature.dapps.detail.utils.DAppMethod
import com.crypto.defi.feature.dapps.detail.utils.WebAppInterface
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DAppDetailScreen(
  dAppViewModel: DAppDetailViewModel = hiltViewModel(),
  chainId: Int,
  dAppUrl: String,
  dAppRpc: String,
  popBack: () -> Unit
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val provideJs by remember {
    mutableStateOf(context.resources.openRawResource(R.raw.trust_min).bufferedReader().use {
      it.readText()
    })
  }
  val initJs by remember {
    mutableStateOf(
      """
      (function() {
            var config = {                
                ethereum: {
                    chainId: $chainId,
                    rpcUrl: "$dAppRpc"
                },
                solana: {
                    cluster: "mainnet-beta",
                },
                isDebug: true
            };
            trustwallet.ethereum = new trustwallet.Provider(config);
            trustwallet.solana = new trustwallet.SolanaProvider(config);
            trustwallet.postMessage = (json) => {
                window._tw_.postMessage(JSON.stringify(json));
            }
            window.ethereum = trustwallet.ethereum;
        })();
    """.trimIndent()
    )
  }

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
            view?.also { webView ->
              webView.evaluateJavascript(provideJs, null)
              webView.evaluateJavascript(initJs, null)
              dAppViewModel.requestAccount(webView)
            }
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
          webView.addJavascriptInterface(WebAppInterface { message ->
            scope.launchWithHandler(Dispatchers.Main) {
              when (message.method) {
                DAppMethod.REQUESTACCOUNTS -> {
                  dAppViewModel.setAddress(webView)
                  dAppViewModel.sendAddress(webView, message.methodId)
                }
                else -> Unit
              }
            }
          }, "_tw_")
        }
      )
    }
  }
}