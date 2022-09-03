import com.crypto.configuration.dependencies.AndroidDeps
import com.crypto.configuration.dependencies.KotlinDeps
import com.crypto.configuration.hiltDependencies

plugins {
    kotlin("plugin.serialization") version "1.7.10"
}

dependencies {
    api(AndroidDeps.coreKtx)

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

    hiltDependencies()
}
