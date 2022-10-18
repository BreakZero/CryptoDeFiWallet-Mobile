package com.crypto.defi.feature.dapps.detail.utils

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.crypto.core.extensions.hexStringToByteArray
import com.crypto.core.extensions.toHexString
import org.json.JSONObject
import timber.log.Timber
import wallet.core.jni.Curve
import wallet.core.jni.PrivateKey

class WebAppInterface(
  private val onResult: (MessageInfo) -> Unit
) {
  @JavascriptInterface
  fun postMessage(json: String) {
    val obj = JSONObject(json)
    val id = obj.getLong("id")
    Timber.tag("=====").v(obj.toString())
    when (val method = DAppMethod.fromValue(obj.getString("name"))) {
      DAppMethod.REQUESTACCOUNTS -> {
        onResult.invoke(
          MessageInfo(
            title = "Request Accounts",
            methodId = id,
            data = "DApp need to get your address",
            method = method
          )
        )
      }
      DAppMethod.SIGNTRANSACTION -> {
        val param = obj.getJSONObject("object")
        val data = param.getString("data")
        val from = param.getString("from")
        val to = param.getString("to")
        onResult.invoke(
          MessageInfo(
            title = "Confirm Transaction",
            methodId = id,
            data = data,
            from = from,
            to = to,
            method = method
          )
        )
      }
      DAppMethod.SIGNMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id, data, method, addPrefix = false)
      }
      DAppMethod.SIGNPERSONALMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id, data, method, addPrefix = true)
      }
      DAppMethod.SIGNTYPEDMESSAGE -> {
        val data = extractMessage(obj)
        val raw = extractRaw(obj)
        handleSignTypedMessage(id, data, method, raw)
      }
      DAppMethod.SWITCHETHEREUMCHAIN -> {
        onResult.invoke(
          MessageInfo(
            title = "SwichChain",
            methodId = id,
            data = "switch to polygon",
            method = method
          )
        )
      }
      else -> {
        onResult.invoke(
          MessageInfo(
            title = "Errors",
            methodId = id,
            data = "$method not implemented",
            method = method
          )
        )
      }
    }
  }

  private fun extractMessage(json: JSONObject): ByteArray {
    val param = json.getJSONObject("object")
    Timber.tag("Easy").d("=== $param")
    val data = param.getString("data")
    return data.hexStringToByteArray()
  }

  private fun extractRaw(json: JSONObject): String {
    val param = json.getJSONObject("object")
    return param.getString("raw")
  }

  private fun handleSignMessage(id: Long, data: ByteArray, method: DAppMethod, addPrefix: Boolean) {
    Timber.d("id: $id, data: $data, addPrefix: $addPrefix ")
    onResult.invoke(
      MessageInfo(
        title = "Sign Message",
        methodId = id,
        data = data.toHexString(),
        method = method
      )
    )
  }

  private fun handleSignTypedMessage(id: Long, data: ByteArray, method: DAppMethod, raw: String) {
    Timber.d("raw: $raw")
    onResult.invoke(
      MessageInfo(
        title = "Sign Message",
        methodId = id,
        data = signEthereumMessage(data, false),
        method = method
      )
    )
  }

  private fun signEthereumMessage(message: ByteArray, addPrefix: Boolean): String {
    var data = message
    if (addPrefix) {
      val messagePrefix = "\u0019Ethereum Signed Message:\n"
      val prefix = (messagePrefix + message.size).toByteArray()
      val result = ByteArray(prefix.size + message.size)
      System.arraycopy(prefix, 0, result, 0, prefix.size)
      System.arraycopy(message, 0, result, prefix.size, message.size)
      data = wallet.core.jni.Hash.keccak256(result)
    }

    /*val signatureData = privateKey.sign(data, Curve.SECP256K1)
      .apply {
        (this[this.size - 1]) = (this[this.size - 1] + 27).toByte()
      }
    return signatureData.toHexString()*/
    return "kdsafjdkj"
  }
}