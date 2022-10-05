package com.crypto.defi.feature.nfts

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.LoadingIndicator
import com.crypto.resource.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainNFTsScreen(
    nftsViewModel: NFTsViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = {
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
                        modifier = Modifier
                            .padding(end = MaterialTheme.Spacing.medium)
                            .clickable {
                            },
                        painter = painterResource(id = R.drawable.ic_scanner),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                },
                title = {
                    Column {
                        Text(
                            text = "Wallet Name",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = stringResource(id = R.string.avatar_wallet_layout__view_settings),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        val nftUiState by nftsViewModel.assetsByGroup.collectAsState()
        AnimatedContent(targetState = true, transitionSpec = {
            fadeIn(animationSpec = tween(300, 300)) with fadeOut(
                animationSpec = tween(
                    300,
                    300
                )
            )
        }) {
            if (nftUiState.isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    LoadingIndicator(animating = true)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(MaterialTheme.Spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.medium)
                ) {
                    items(nftUiState.ntfs) { group ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.Spacing.space12)
                            ) {
                                Row() {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(MaterialTheme.Spacing.space48)
                                            .clip(CircleShape),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(group.logoUrl)
                                            .placeholder(R.drawable.avatar_generic_1)
                                            .error(R.drawable.avatar_generic_1)
                                            .crossfade(true)
                                            .build(), contentDescription = null
                                    )
                                    Column(
                                        modifier = Modifier
                                            .height(MaterialTheme.Spacing.space48)
                                            .padding(
                                                start = MaterialTheme.Spacing.small,
                                                top = MaterialTheme.Spacing.extraSmall,
                                                bottom = MaterialTheme.Spacing.extraSmall
                                            ),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = group.contractName)
                                        Text(
                                            text = group.contractAddress,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(MaterialTheme.Spacing.small))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.space12)
                                ) {
                                    items(group.assets) { asset ->
                                        AsyncImage(
                                            modifier = Modifier
                                                .size(MaterialTheme.Spacing.space128)
                                                .clip(RoundedCornerShape(MaterialTheme.Spacing.space24))
                                                .background(color = MaterialTheme.colorScheme.surface),
                                            contentScale = ContentScale.Crop,
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(asset.nftscanUri ?: asset.imageUri)
                                                .placeholder(R.drawable.avatar_generic_1)
                                                .error(R.drawable.avatar_generic_1)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null
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