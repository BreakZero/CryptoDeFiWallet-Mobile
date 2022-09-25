package com.crypto.configuration.dependencies

object AndroidDeps {

    const val coreKtx = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.5.0"
    const val activity_compose = "androidx.activity:activity-compose:1.5.1"
    const val securitys = "androidx.security:security-crypto:1.0.0"

    const val zxing = "com.google.zxing:core:3.5.0"
    const val timber = "com.jakewharton.timber:timber:5.0.1"

    object Lifecycle {
        private const val lifecycle_version = "2.5.1"
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
        const val livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val viewmodel_savedstate =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val common_java8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
        const val service = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
    }

    object Hilt {
        private const val version = "2.43.2"
        const val hilt_android = "com.google.dagger:hilt-android:$version"
        const val hilt_android_compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hilt_compiler = "androidx.hilt:hilt-compiler:1.0.0"
        const val hilt_nav_compose = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val hilt_work = "androidx.hilt:hilt-work:1.0.0"
    }

    object Room {
        private const val version = "2.4.2"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
    }

    object DataStore {
        private const val version = "1.0.0"
        const val core = "androidx.datastore:datastore:$version"
        const val preferences = "androidx.datastore:datastore-preferences:$version"
    }
    object Camera {
        private const val camera_version = "1.1.0-beta03"
        const val camera2 = "androidx.camera:camera-camera2:$camera_version"
        const val lifecycle = "androidx.camera:camera-lifecycle:$camera_version"
        const val view = "androidx.camera:camera-view:$camera_version"
    }
}