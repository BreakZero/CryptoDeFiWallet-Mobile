package com.crypto.configuration

import com.android.build.gradle.LibraryExtension
import com.crypto.configuration.dependencies.ComposeDeps
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

class CryptoModuleConfigPlugin : Plugin<Project> {
    private val ignoreList = listOf("app", "onboarding")

    override fun apply(target: Project) {
        if (target.name !in ignoreList) {
            target.beforeEvaluate { applyPlugin() }
            target.afterEvaluate {
                extensions.getByType(LibraryExtension::class).run {
                    moduleConfig()
                }
            }
        }
    }

    private fun Project.applyPlugin() {
        apply(plugin = "com.android.library")
        apply(plugin = "kotlin-android")
        apply(plugin = "kotlin-kapt")
        apply(plugin = "kotlin-parcelize")
    }

    @Suppress("UnstableApiUsage")
    private fun LibraryExtension.moduleConfig() {
        compileSdk = AndroidBuildConfig.compileSdkVersion
        packagingOptions {
            resources.excludes.add("META-INF/INDEX.LIST")
        }
        lint {
            isCheckDependencies = true
        }

        defaultConfig {
            minSdk = AndroidBuildConfig.minSdkVersion
            targetSdk = AndroidBuildConfig.targetSdkVersion
            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        }
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            debug {
                isMinifyEnabled = false
                isTestCoverageEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        buildFeatures.also {
            it.compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = ComposeDeps.compiler_version
        }
    }
}

fun keyStoreProperties(): Properties {
    val properties = Properties()
    val keyProperties = File("./keystore", "keystore.properties")

    if (keyProperties.isFile) {
        InputStreamReader(FileInputStream(keyProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}