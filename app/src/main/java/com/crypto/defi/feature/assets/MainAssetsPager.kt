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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiActionBar
import com.crypto.defi.feature.assets.components.AssetCard
import com.crypto.defi.feature.assets.components.CollapsableToolbar
import com.crypto.defi.models.domain.Asset
import com.crypto.resource.R

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MainAssetsPager(
    assetsViewModel: MainAssetsViewModel = hiltViewModel()
) {
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
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    topEnd = MaterialTheme.Spacing.space24,
                                    topStart = MaterialTheme.Spacing.space24
                                )
                            )
                            .background(MaterialTheme.colorScheme.surface),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxHeight(),
                            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items((0..18).map {
                                Asset(
                                    slug = it.toString(),
                                    name = "Name: $it",
                                    symbol = "Symbol: $it",
                                    iconUrl = ""
                                )
                            }) { asset ->
                                AssetCard(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    asset = asset
                                )
                            }
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items((0..5).map { it }) { it ->
                                        Box(
                                            modifier = Modifier
                                                .size(128.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(painter = painterResource(id = R.drawable.card_small_orange), contentDescription = null)
                                            Text(text = "Item $it")
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