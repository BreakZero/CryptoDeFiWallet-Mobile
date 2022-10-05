import com.crypto.configuration.dependencies.AndroidDeps
import com.crypto.configuration.dependencies.KotlinDeps
import com.crypto.configuration.hiltDependencies

plugins {
    kotlin("plugin.serialization") version "1.7.10"
}

configurations {
    all {
        exclude("androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }
}

dependencies {
    api(AndroidDeps.coreKtx)
    api(AndroidDeps.securitys)

    api(AndroidDeps.timber)

    api(KotlinDeps.kotlinx_datetime)
    api(KotlinDeps.Coroutine.core)
    api(KotlinDeps.Coroutine.android)
    api(KotlinDeps.Ktor.core)
    api(KotlinDeps.Ktor.android)
    api(KotlinDeps.Ktor.logging)
    api(KotlinDeps.Ktor.negotiation)
    api(KotlinDeps.Ktor.json)
    api(KotlinDeps.kotlinx_serialization)

    api(AndroidDeps.DataStore.preferences)
    api(AndroidDeps.DataStore.core)

    api("com.trustwallet:wallet-core:3.0.0")

    hiltDependencies()
}

kapt {
    correctErrorTypes = true
}
