package com.crypto.defi.feature.assets

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.model.NetworkStatus
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiBoxWithConstraints
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.feature.assets.components.AssetCard
import com.crypto.defi.feature.assets.components.HomeAssetsMotionLayout
import com.crypto.defi.navigations.TransactionListNavigation
import com.crypto.resource.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MainAssetsPager(
    assetsViewModel: MainAssetsViewModel = hiltViewModel(),
    navigateTo: (NavigationCommand) -> Unit
) {
    val context = LocalContext.current
    val assetState = assetsViewModel.assetState
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        //
                    }) {
                        Image(
                            modifier = Modifier.size(MaterialTheme.Spacing.space48),
                            painter = painterResource(id = R.drawable.avatar_generic_1),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Icon(
                        modifier = Modifier.padding(end = MaterialTheme.Spacing.medium),
                        painter = painterResource(id = R.drawable.ic_scanner),
                        contentDescription = null
                    )
                },
                title = {
                    Column {
                        Text(
                            text = "Wallet Name",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = stringResource(id = R.string.avatar_wallet_layout__view_settings),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            )
        }) {
        DeFiBoxWithConstraints { progress, isExpanded ->
            HomeAssetsMotionLayout(targetValue = progress) {
                AnimatedContent(targetState = true, transitionSpec = {
                    fadeIn(animationSpec = tween(300, 300)) with fadeOut(
                        animationSpec = tween(
                            300,
                            300
                        )
                    )
                }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    topEnd = MaterialTheme.Spacing.space24,
                                    topStart = MaterialTheme.Spacing.space24
                                )
                            )
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        when (assetState.assetsResult) {
                            is NetworkStatus.Loading -> {
                                CircularProgressIndicator()
                            }
                            is NetworkStatus.Error -> {
                                Text(
                                    text = assetState.assetsResult.message,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            is NetworkStatus.Success -> {
                                SwipeRefresh(
                                    state = rememberSwipeRefreshState(assetState.onRefreshing),
                                    swipeEnabled = isExpanded,
                                    onRefresh = {
                                        assetsViewModel.onRefresh()
                                    }
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxHeight(),
                                        contentPadding = PaddingValues(
                                            vertical = MaterialTheme.Spacing.medium,
                                            horizontal = MaterialTheme.Spacing.small
                                        ),
                                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small)
                                    ) {
                                        items(assetState.assetsResult.data) { asset ->
                                            AssetCard(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                asset = asset
                                            ) {
                                                navigateTo(TransactionListNavigation.destination(it.code))
                                            }
                                        }
                                        item {
                                            LazyRow(
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    MaterialTheme.Spacing.space12
                                                )
                                            ) {
                                                items(assetState.promoCard) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(MaterialTheme.Spacing.space128)
                                                    ) {
                                                        Image(
                                                            painter = painterResource(id = it.backgroundRes),
                                                            contentDescription = null
                                                        )
                                                        Text(
                                                            modifier = Modifier.padding(
                                                                MaterialTheme.Spacing.extraSmall
                                                            ),
                                                            text = it.title.asString(context),
                                                            color = Color.White
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}