package com.crypto.configuration.dependencies

object ComposeDeps {
    const val compiler_version = "1.3.0"
    const val version = "1.2.1"
    const val ui = "androidx.compose.ui:ui:$version"
    const val preview = "androidx.compose.ui:ui-tooling-preview:$version"
    const val icons = "androidx.compose.material:material-icons-extended:$version"
    const val coil = "io.coil-kt:coil-compose:2.2.0"
    const val navigation = "androidx.navigation:navigation-compose:2.4.2"
    const val paging = "androidx.paging:paging-compose:1.0.0-alpha14"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout-compose:1.0.1"

    const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"
    const val uiTooling = "androidx.compose.ui:ui-tooling:$version"

    object Material3 {
        private const val version = "1.0.0-alpha16"
        const val material3 = "androidx.compose.material3:material3:$version"
        const val material3_window_size = "androidx.compose.material3:material3-window-size-class:$version"
    }

    object Accompanist {
        private const val version = "0.25.1"
        const val windowInsets = "com.google.accompanist:accompanist-insets:$version"
        const val uiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val permission = "com.google.accompanist:accompanist-permissions:$version"
        const val navigation = "com.google.accompanist:accompanist-navigation-material:$version"
        const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:$version"
        const val webView = "com.google.accompanist:accompanist-webview:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pager_indicators = "com.google.accompanist:accompanist-pager-indicators:$version"
    }
}