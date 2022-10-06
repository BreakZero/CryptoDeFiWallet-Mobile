package com.crypto.defi.feature.nfts.group

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.LoadingIndicator
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun NftGroupScreen(
    nftGroupViewModel: NftGroupViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DeFiAppBar() {
                onBack()
            }
        }
    ) {
        val nftGroupsUiState by nftGroupViewModel.assetsByGroup.collectAsState()
        AnimatedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            targetState = true, transitionSpec = {
                fadeIn(animationSpec = tween(300, 300)) with fadeOut(
                    animationSpec = tween(
                        300,
                        300
                    )
                )
            }) {
            if (nftGroupsUiState.isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    LoadingIndicator(animating = true)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(MaterialTheme.Spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.medium)
                ) {
                    items(nftGroupsUiState.nftGroups) { group ->
                        GroupItemView(group = group)
                    }
                }
            }
        }
    }
}
