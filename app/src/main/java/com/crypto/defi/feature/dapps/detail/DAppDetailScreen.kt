package com.crypto.defi.feature.dapps.detail

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.defi.BuildConfig
import com.crypto.defi.feature.dapps.detail.utils.DAppMethod
import com.crypto.defi.feature.dapps.detail.utils.WebAppInterface
import com.easy.defi.app.core.common.extensions.launchWithHandler
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DAppDetailScreen(
  dAppViewModel: DAppDetailViewModel = hiltViewModel(),
  chainId: Int,
  dAppUrl: String,
  dAppRpc: String,
  popBack: () -> Unit,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val provideJs by remember {
    mutableStateOf(
      context.resources.openRawResource(R.raw.trust_min).bufferedReader().use {
        it.readText()
      },
    )
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
      """.trimIndent(),
    )
  }
  val bottomSheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true,
  )

  val dAppUiState by dAppViewModel.dAppState.collectAsState()
  ModalBottomSheetLayout(
    sheetState = bottomSheetState,
    sheetContent = {
      ActionConfirmView(
        title = dAppUiState.title,
        message = dAppUiState.data,
        onConfirm = {
          scope.launch {
            bottomSheetState.hide()
          }
        },
        onReject = {
          scope.launch {
            bottomSheetState.hide()
          }
        },
      )
    },
    sheetElevation = MaterialTheme.Spacing.small,
    sheetShape = RoundedCornerShape(
      topEnd = MaterialTheme.Spacing.space24,
      topStart = MaterialTheme.Spacing.space24,
    ),
  ) {
    val url by remember {
      mutableStateOf(dAppUrl)
    }
    val navigator = rememberWebViewNavigator()
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        DeFiAppBar(
          actions = {
            IconButton(
              modifier = Modifier,
              onClick = { /*TODO*/ },
            ) {
              Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
              )
            }
            IconButton(
              modifier = Modifier,
              onClick = { /*TODO*/ },
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
              )
            }
          },
        ) {
          if (navigator.canGoBack) {
            navigator.navigateBack()
          } else {
            popBack()
          }
        }
      },
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
      ) {
        val webViewState = rememberWebViewState(url = url)
        val loadingState = webViewState.loadingState
        if (loadingState is LoadingState.Loading) {
          LinearProgressIndicator(
            color = MaterialTheme.colorScheme.tertiary,
            progress = loadingState.progress,
            modifier = Modifier.fillMaxWidth(),
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
            webView.addJavascriptInterface(
              WebAppInterface { message ->
                dAppViewModel.updateMessage(message)
                scope.launchWithHandler(Dispatchers.Main) {
                  when (message.method) {
                    DAppMethod.REQUESTACCOUNTS -> {
                      dAppViewModel.setAddress(webView)
                      dAppViewModel.sendAddress(webView, message.methodId)
                    }
                    DAppMethod.SWITCHETHEREUMCHAIN -> {
                      bottomSheetState.show()
                      dAppViewModel.switchChain(webView, "137")
                    }
                    else -> Unit
                  }
                }
              },
              "_tw_",
            )
          },
        )
      }
    }
  }
}

@Composable
fun ActionConfirmView(
  title: String,
  message: String,
  modifier: Modifier = Modifier,
  onConfirm: () -> Unit,
  onReject: () -> Unit,
) {
  Column(
    modifier = modifier.padding(MaterialTheme.Spacing.medium),
  ) {
    Text(
      text = title,
      modifier = Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = message,
      modifier = Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.bodyMedium,
    )
    Row(modifier = Modifier.fillMaxWidth()) {
      Button(modifier = Modifier.weight(1f), onClick = {
        onConfirm()
      },) {
        Text(text = "Confirm")
      }
      Button(modifier = Modifier.weight(1f), onClick = {
        onReject()
      },) {
        Text(text = "Cancel")
      }
    }
  }
}
