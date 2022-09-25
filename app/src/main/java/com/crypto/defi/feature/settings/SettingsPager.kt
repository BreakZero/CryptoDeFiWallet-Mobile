package com.crypto.defi.feature.settings

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsPager() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val scrollableState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(state = scrollableState, orientation = Orientation.Vertical)
        ) {

        }
    }
}