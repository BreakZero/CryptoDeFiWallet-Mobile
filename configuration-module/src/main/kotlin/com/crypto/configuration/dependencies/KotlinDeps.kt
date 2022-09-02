package com.crypto.configuration.dependencies

class KotlinDeps {
    object Ktor {
        private const val ktor_version = "2.0.0"
        const val client_core = "io.ktor:ktor-client-core:$ktor_version"
        const val client_log = "io.ktor:ktor-client-logging:$ktor_version"
        const val client_negotiation = "io.ktor:ktor-client-content-negotiation:$ktor_version"
        const val client_android = "io.ktor:ktor-client-android:$ktor_version"
        const val client_gson  = "io.ktor:ktor-serialization-gson:$ktor_version"
        const val client_serializer  = "io.ktor:ktor-serialization-kotlinx-json:$ktor_version"
        const val client_test = "io.ktor:ktor-client-mock:$ktor_version"
    }
}