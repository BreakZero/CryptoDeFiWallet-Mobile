plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("crypto.module.config") apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
        classpath("com.google.firebase:perf-plugin:1.4.1")
    }
}

subprojects {
    this.apply(from = "${rootProject.rootDir}/lint.gradle.kts")
    this.apply(plugin = "crypto.module.config")
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
        kotlinOptions {
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
