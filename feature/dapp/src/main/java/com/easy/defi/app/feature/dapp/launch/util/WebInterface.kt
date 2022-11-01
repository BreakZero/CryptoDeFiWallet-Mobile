package com.easy.defi.app.feature.dapp.launch.util

import android.webkit.JavascriptInterface
import com.easy.defi.app.core.common.extensions.hexStringToByteArray
import com.easy.defi.app.core.common.extensions.toHexString
import org.json.JSONObject
import timber.log.Timber

internal class WebInterface(
  private val result: (MessageData) -> Unit
) {
  @JavascriptInterface
  fun postMessage(json: String) {
    val obj = JSONObject(json)
    val id = obj.getLong("id")
    Timber.tag("=====").v(obj.toString())
    when (val method = ActionMethod.fromValue(obj.getString("name"))) {
      ActionMethod.REQUESTACCOUNTS -> {
        result.invoke(
          MessageData(
            title = "Request Accounts",
            methodId = id,
            data = "DApp need to get your address",
            method = method
          )
        )
      }
      ActionMethod.SIGNTRANSACTION -> {
        val param = obj.getJSONObject("object")
        val data = param.getString("data")
        val from = param.getString("from")
        val to = param.getString("to")
        result.invoke(
          MessageData(
            title = "Confirm Transaction",
            methodId = id,
            data = data,
            from = from,
            to = to,
            method = method
          )
        )
      }
      ActionMethod.SIGNMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id, data, method, addPrefix = false)
      }
      ActionMethod.SIGNPERSONALMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id, data, method, addPrefix = true)
      }
      ActionMethod.SIGNTYPEDMESSAGE -> {
        val data = extractMessage(obj)
        val raw = extractRaw(obj)
        handleSignTypedMessage(id, data, method, raw)
      }
      ActionMethod.SWITCHETHEREUMCHAIN -> {
        result.invoke(
          MessageData(
            title = "SwichChain",
            methodId = id,
            data = "switch to polygon",
            method = method
          )
        )
      }
      else -> {
        result.invoke(
          MessageData(
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

  private fun handleSignMessage(id: Long, data: ByteArray, method: ActionMethod, addPrefix: Boolean) {
    Timber.d("id: $id, data: $data, addPrefix: $addPrefix ")
    result.invoke(
      MessageData(
        title = "Sign Message",
        methodId = id,
        data = data.toHexString(),
        method = method
      )
    )
  }

  private fun handleSignTypedMessage(id: Long, data: ByteArray, method: ActionMethod, raw: String) {
    Timber.d("raw: $raw")
    result.invoke(
      MessageData(
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
//      data = wallet.core.jni.Hash.keccak256(result)
    }

    /*val signatureData = privateKey.sign(data, Curve.SECP256K1)
      .apply {
        (this[this.size - 1]) = (this[this.size - 1] + 27).toByte()
      }
    return signatureData.toHexString()*/
    return "kdsafjdkj"
  }
}
