package com.easy.defi.app.feature.dapp.launch.util

import android.webkit.WebView

internal fun WebView.sendError(chain: String = "ethereum", methodId: Long, message: String) {
  val script = "window.$chain.sendError($methodId, \"$message\")"
  this.post {
    this.evaluateJavascript(script) {}
  }
}

internal fun WebView.sendResult(chain: String = "ethereum", methodId: Long, message: String) {
  val script = "window.$chain.sendResponse($methodId, \"$message\")"
  this.post {
    this.evaluateJavascript(script) {}
  }
}

internal fun WebView.sendResults(chain: String = "ethereum", methodId: Long, messages: List<String>) {
  val message = messages.joinToString(separator = ",")
  val script = "window.$chain.sendResponse($methodId, \"$message\")"
  this.post {
    this.evaluateJavascript(script) {}
  }
}
