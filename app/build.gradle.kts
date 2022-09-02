import com.crypto.configuration.*
import com.crypto.configuration.dependencies.AndroidDeps

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.firebase-perf")
}
android {
    compileSdk = AndroidBuildConfig.compileSdkVersion

    defaultConfig {
        applicationId = "com.crypto.defi"
        minSdk = AndroidBuildConfig.minSdkVersion
        targetSdk = AndroidBuildConfig.targetSdkVersion
        versionCode = AndroidBuildConfig.versionCode
        versionName = AndroidBuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary true
//        }
    }

    val keyProperties = keyStoreProperties()
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
        create("release") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("google/protobuf/*.proto")
        }
    }
}

dependencies {
    implementation(AndroidDeps.coreKtx)
    implementation(AndroidDeps.Lifecycle.runtime)
    implementation(AndroidDeps.appcompat)
    implementation(AndroidDeps.activity_compose)

    composeUI()
    hiltDependencies()

    unitTestDependencies()
    androidTestDependencies()
}
