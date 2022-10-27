package com.crypto.configuration.dependencies

object KotlinDeps {
  const val kotlinx_serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"
  const val kotlinx_datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"

  object Coroutine {
    private const val version = "1.6.0"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
  }

  object Ktor {
    private const val version = "2.0.0"
    const val core = "io.ktor:ktor-client-core:$version"
    const val logging = "io.ktor:ktor-client-logging:$version"
    const val negotiation = "io.ktor:ktor-client-content-negotiation:$version"
    const val android = "io.ktor:ktor-client-android:$version"
    const val gson = "io.ktor:ktor-serialization-gson:$version"
    const val json = "io.ktor:ktor-serialization-kotlinx-json:$version"
    const val mock = "io.ktor:ktor-client-mock:$version"
  }
}