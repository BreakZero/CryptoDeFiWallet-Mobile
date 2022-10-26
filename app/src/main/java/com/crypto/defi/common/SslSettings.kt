package com.crypto.defi.common

import android.content.Context
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SslSettings {
  fun getKeyStore(context: Context): KeyStore {
    val assetsManager = context.assets
    val keyStoreFile = assetsManager.open("android.jks")
    val keyStorePassword = "foobar".toCharArray()
    val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(keyStoreFile, keyStorePassword)
    return keyStore
  }

  fun getTrustManagerFactory(context: Context): TrustManagerFactory? {
    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(getKeyStore(context))
    return trustManagerFactory
  }

  fun getSslContext(context: Context): SSLContext? {
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, getTrustManagerFactory(context)?.trustManagers, null)
    return sslContext
  }

  fun getTrustManager(context: Context): X509TrustManager {
    return getTrustManagerFactory(context)?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
  }
}
