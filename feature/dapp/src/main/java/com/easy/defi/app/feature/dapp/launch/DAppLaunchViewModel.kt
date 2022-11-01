package com.easy.defi.app.feature.dapp.launch

import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.data.di.annotations.Ethereum
import com.easy.defi.app.core.data.repository.ChainRepository
import com.easy.defi.app.feature.dapp.launch.util.ActionMethod
import com.easy.defi.app.feature.dapp.launch.util.MessageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DAppLaunchViewModel @Inject constructor(
  @Ethereum evmChainRepository: ChainRepository
): ViewModel() {
  private val ethAddress = evmChainRepository.address.orEmpty()

  private val _message = MutableStateFlow(MessageData.Empty)

  val uiState = _message.map {
    DAppLaunchState(
      title = it.title,
      method = it.method,
      methodId = it.methodId,
      data = it.data
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), DAppLaunchState())

  internal fun updateMessage(message: MessageData) {
    _message.update { message }
  }

  fun requestAccount(webView: WebView) {
    val script = "window.ethereum.request({method: \"eth_requestAccounts\"});"
    webView.evaluateJavascript(script) {}
  }

  fun setAddress(webView: WebView) {
    val script = "window.ethereum.setAddress(\"$ethAddress\");"
    webView.evaluateJavascript(script) {}
  }

  fun sendAddress(webView: WebView, methodId: Long) {
    val script = "window.ethereum.sendResponse($methodId, [\"$ethAddress\"])"
    webView.evaluateJavascript(script) {}
  }

  fun switchChain(webView: WebView, chainId: String) {
    val script = "window.ethereum.emitChainChanged($chainId);"
    val script1 = "window.ethereum.emitConnect($chainId);"
    webView.evaluateJavascript(script) {}
    webView.evaluateJavascript(script1) {}
  }
}

data class DAppLaunchState(
  val title: String = "",
  val method: ActionMethod = ActionMethod.UNKNOWN,
  val methodId: Long = -1L,
  val data: String = ""
)
