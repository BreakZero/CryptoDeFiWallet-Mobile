import com.crypto.configuration.roomDependencies
import com.crypto.configuration.hiltDependencies
import com.crypto.configuration.unitTestDependencies
import com.crypto.configuration.androidTestDependencies

plugins {
    kotlin("plugin.serialization") version "1.7.10"
}
apply(from = "${rootProject.rootDir}/jacoco.gradle.kts")
configurations {
    all {
        exclude("androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }
}

dependencies {
    implementation(project(":core"))

    implementation("net.zetetic:android-database-sqlcipher:4.5.0")

    roomDependencies()
    hiltDependencies()
    unitTestDependencies()
    androidTestDependencies()
}