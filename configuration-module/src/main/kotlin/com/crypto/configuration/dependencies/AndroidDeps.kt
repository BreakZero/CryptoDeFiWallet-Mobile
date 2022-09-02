package com.crypto.configuration.dependencies

object AndroidDeps {

    const val coreKtx = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.5.0"
    const val activity_compose = "androidx.activity:activity-compose:1.5.1"

    object Lifecycle {
        private const val lifecycle_version = "2.5.1"
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val viewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
        const val service = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
    }

    object Hilt {
        private const val version = "2.43.2"
        const val hilt_android = "com.google.dagger:hilt-android:$version"
        const val hilt_android_compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hilt_lifecycle_viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val hilt_compiler = "androidx.hilt:hilt-compiler:1.0.0"
        const val hilt_nav_compose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object Room {
        private const val room_version = "2.4.2"
        const val roomRuntime = "androidx.room:room-runtime:$room_version"
        const val roomCompiler = "androidx.room:room-compiler:$room_version"
        const val roomKtx = "androidx.room:room-ktx:$room_version"
    }

    object Camera {
        private const val camera_version = "1.1.0-beta03"
        const val camera = "androidx.camera:camera-camera2:$camera_version"
        const val cameraLife = "androidx.camera:camera-lifecycle:$camera_version"
        const val cameraView = "androidx.camera:camera-view:$camera_version"
    }
}