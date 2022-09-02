package com.crypto.configuration.dependencies

object AndroidDeps {
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