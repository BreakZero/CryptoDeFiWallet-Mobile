package com.crypto.defi.feature.settings

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.routers.NavigationCommand

@Composable
fun SettingsPager(
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    Scaffold(
        topBar = {
            DeFiAppBar() {
                navigateUp()
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        val scrollableState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(state = scrollableState, orientation = Orientation.Vertical)
        ) {

        }
    }
}