package com.crypto.defi.feature.dapps.detail

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.chains.ChainManager
import com.crypto.defi.feature.dapps.detail.utils.MessageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DAppDetailViewModel @Inject constructor(
  private val chainManager: ChainManager
) : ViewModel() {
  private val evmAddress = chainManager.evmAddress()

  private val _currMessage = MutableStateFlow(MessageInfo.Empty)

  val dAppState = _currMessage.map {
    DAppState(
      title = it.title,
      method = it.method,
      methodId = it.methodId,
      data = it.data
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DAppState())

  fun updateMessage(messageInfo: MessageInfo) {
    _currMessage.update {
      messageInfo
    }
  }

  fun requestAccount(webView: WebView) {
    val script = "window.ethereum.request({method: \"eth_requestAccounts\"});"
    webView.evaluateJavascript(script) {}
  }

  fun setAddress(webView: WebView) {
    val script = "window.ethereum.setAddress(\"$evmAddress\");"
    webView.evaluateJavascript(script) {}
  }

  fun sendAddress(webView: WebView, methodId: Long) {
    val script = "window.ethereum.sendResponse(${methodId}, [\"$evmAddress\"])"
    webView.evaluateJavascript(script) {}
  }

  fun switchChain(webView: WebView, chainId: String) {
    val script = "window.ethereum.emitChainChanged($chainId);"
    val script1 = "window.ethereum.emitConnect($chainId);"
    webView.evaluateJavascript(script) {}
    webView.evaluateJavascript(script1) {}
  }
}

