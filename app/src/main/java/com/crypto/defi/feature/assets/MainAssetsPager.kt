package com.crypto.defi.feature.assets

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.crypto.core.ui.composables.DeFiActionBar
import com.crypto.defi.feature.assets.components.CollapsableToolbar
import com.crypto.resource.R

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainAssetsPager() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            DeFiActionBar(
                modifier = Modifier
                    .zIndex(3.0f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                navIcon = R.drawable.avatar_generic_1,
                menuIcons = listOf(R.drawable.ic_scanner),
                title = "Wallet Name",
                subtitle = stringResource(id = R.string.avatar_wallet_layout__view_settings),
                onNavClick = {

                },
                onMenuClick = {

                }
            )
            CollapsableToolbar {
                AnimatedContent(
                    targetState = true,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300, 300)) with
                                fadeOut(animationSpec = tween(300, 300))
                    }
                ) { state ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "main assets $state")
                    }
                }
            }
        }
    }
}