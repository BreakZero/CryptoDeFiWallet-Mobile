package com.crypto.defi.feature.dapps.detail

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import com.crypto.defi.chains.ChainManager
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject
@HiltViewModel
class DAppDetailViewModel @Inject constructor(
  private val chainManager: ChainManager
) : ViewModel() {
  private val evmAddress = chainManager.evmAddress()

  fun requestAccount(webView: WebView) {
    val script = "window.ethereum.request({method: \"eth_requestAccounts\"});"
    webView.evaluateJavascript(script) {
      Timber.tag("=====").v(it)
    }
  }

  fun setAddress(webView: WebView) {
    val script = "window.ethereum.setAddress(\"$evmAddress\");"
    webView.evaluateJavascript(script) {
      Timber.tag("=====").v(it)
    }
  }

  fun sendAddress(webView: WebView, methodId: Long) {
    val script = "window.ethereum.sendResponse(${methodId}, [\"$evmAddress\"])"
    webView.evaluateJavascript(script) {
      Timber.tag("=====").v(it)
    }
  }
}

