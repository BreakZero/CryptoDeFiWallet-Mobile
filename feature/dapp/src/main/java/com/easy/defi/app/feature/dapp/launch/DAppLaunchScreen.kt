package com.easy.defi.app.feature.dapp.launch

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.feature.dapp.R
import com.easy.defi.app.feature.dapp.launch.util.ActionMethod
import com.easy.defi.app.feature.dapp.launch.util.WebInterface
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import timber.log.Timber

@OptIn(ExperimentalLifecycleComposeApi::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun DAppLaunchScreen(
  dAppLaunchViewModel: DAppLaunchViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {
  val context = LocalContext.current
  val message by dAppLaunchViewModel.uiState.collectAsState()
  val showConfirm by dAppLaunchViewModel.showConfirmDialog.collectAsStateWithLifecycle()
  var currWebView by remember { mutableStateOf<WebView?>(null) }

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
            currWebView = webView
            webView.evaluateJavascript(provideJs, null)
            webView.evaluateJavascript(dAppLaunchViewModel.initJs, null)
            dAppLaunchViewModel.requestAccount(currWebView!!)
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
            dAppLaunchViewModel.onMessageReceive(message)
          },
          "_tw_"
        )
      }
    )
    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))

    if (showConfirm) {
      ActionAlert(
        title = message.title,
        message = message.data,
        onConfirm = {
          when (message.method) {
            ActionMethod.REQUESTACCOUNTS -> {
              currWebView?.let {
                Timber.tag("=====").v(message.toString())
                dAppLaunchViewModel.setAddress(it)
                dAppLaunchViewModel.sendAddress(it, message.methodId)
              }
            }
            ActionMethod.SWITCHETHEREUMCHAIN -> {
            }
            else -> Unit
          }
        }, onDismiss = {
          dAppLaunchViewModel.dismiss()
        }
      )
    }
  }
}

@Composable
private fun ActionAlert(
  title: String,
  message: String,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = {
      Timber.tag("=====").v("tap outside")
    },
    title = {
      Text(text = title)
    },
    text = {
      Text(message)
    },
    confirmButton = {
      Button(
        onClick = {
          onConfirm()
        }
      ) {
        Text("Approve")
      }
    },
    dismissButton = {
      Button(
        onClick = {
          onDismiss()
        }
      ) {
        Text("Cancel")
      }
    }
  )
}
