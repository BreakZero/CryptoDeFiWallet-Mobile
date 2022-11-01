package com.easy.defi.app.feature.dapp.launch.util

enum class ActionMethod {
  SIGNTRANSACTION,
  SIGNPERSONALMESSAGE,
  SIGNMESSAGE,
  SIGNTYPEDMESSAGE,
  ECRECOVER,
  REQUESTACCOUNTS,
  WATCHASSET,
  ADDETHEREUMCHAIN,
  SWITCHETHEREUMCHAIN,
  UNKNOWN;

  companion object {
    fun fromValue(value: String): ActionMethod {
      return when (value) {
        "signTransaction" -> SIGNTRANSACTION
        "signPersonalMessage" -> SIGNPERSONALMESSAGE
        "signMessage" -> SIGNMESSAGE
        "signTypedMessage" -> SIGNTYPEDMESSAGE
        "ecRecover" -> ECRECOVER
        "requestAccounts" -> REQUESTACCOUNTS
        "watchAsset" -> WATCHASSET
        "addEthereumChain" -> ADDETHEREUMCHAIN
        "switchEthereumChain" -> SWITCHETHEREUMCHAIN
        else -> UNKNOWN
      }
    }
  }
}
