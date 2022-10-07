package com.crypto.core.model

sealed class NetworkStatus<out T : Any> {
  object Loading : NetworkStatus<Nothing>()
  data class Error(val message: String) : NetworkStatus<Nothing>()
  data class Success<T : Any>(val data: T) : NetworkStatus<T>()
}
