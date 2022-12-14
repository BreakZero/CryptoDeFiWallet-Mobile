/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalFoundationApi::class)

package com.easy.defi.feature.asset.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.component.brushBackground
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.ui.extension.rotating
import com.easy.defi.feature.asset.components.AssetCard
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(
  ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
) @Composable fun AssetListScreen(
  assetsViewModel: AssetListViewModel = hiltViewModel(),
  navigateToSettings: () -> Unit,
  navigateToTransactionList: (String) -> Unit
) {
  val context = LocalContext.current
  val assetsUiState by assetsViewModel.assetState.collectAsState()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .brushBackground(
        listOf(
          MaterialTheme.colorScheme.primary,
          MaterialTheme.colorScheme.surface,
          MaterialTheme.colorScheme.surface
        )
      )
  ) {
    TopAppBar(
      colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent
      ),
      navigationIcon = {
        IconButton(onClick = {
          navigateToSettings()
        }) {
          assetsUiState.walletProfile.avator?.let {
            AsyncImage(
              modifier = Modifier
                .size(MaterialTheme.spacing.space48)
                .clip(CircleShape)
                .rotating(2500),
              model = ImageRequest.Builder(LocalContext.current).data(it)
                .placeholder(R.drawable.avatar_generic_1).error(R.drawable.avatar_generic_1)
                .crossfade(true).build(),
              contentDescription = null
            )
          } ?: run {
            Image(
              modifier = Modifier
                .size(MaterialTheme.spacing.space48)
                .clip(CircleShape)
                .rotating(2500),
              painter = painterResource(id = R.drawable.avatar_generic_1),
              contentDescription = null
            )
          }
        }
      },
      actions = {
        Icon(
          modifier = Modifier
            .padding(end = MaterialTheme.spacing.medium)
            .clickable {},
          painter = painterResource(id = R.drawable.ic_scanner),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primaryContainer
        )
      },
      title = {
        Column {
          Text(
            text = assetsUiState.walletProfile.walletName,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primaryContainer
          )
          Text(
            text = stringResource(id = R.string.view_settings),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surfaceVariant
          )
        }
      }
    )
    SwipeRefresh(
      modifier = Modifier
        .fillMaxSize(),
      state = rememberSwipeRefreshState(assetsUiState.onRefreshing),
      swipeEnabled = true,
      onRefresh = {
        assetsViewModel.onRefresh()
      }
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize(),
        contentPadding = PaddingValues(
          vertical = MaterialTheme.spacing.medium,
          horizontal = MaterialTheme.spacing.small
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
      ) {
        item {
          AssetListHeader(
            modifier = Modifier.fillMaxWidth(),
            totalBalance = assetsUiState.totalBalance
          )
        }
        items(items = assetsUiState.assets, key = {
          it.slug
        }) { asset ->
          AssetCard(
            modifier = Modifier
              .fillMaxWidth()
              .animateItemPlacement(),
            asset = asset
          ) {
            navigateToTransactionList(it.slug)
          }
        }
        item {
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
              MaterialTheme.spacing.space12
            )
          ) {
            items(items = assetsUiState.promoCard, key = { promoCard ->
              promoCard.backgroundRes
            }) {
              Card(
                modifier = Modifier
                  .width(MaterialTheme.spacing.space128)
                  .aspectRatio(4 / 3f),
                elevation = CardDefaults.cardElevation(
                  defaultElevation = MaterialTheme.spacing.extraSmall
                ),
                shape = RoundedCornerShape(MaterialTheme.spacing.small)
              ) {
                Box(
                  modifier = Modifier.fillMaxSize()
                ) {
                  Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = it.backgroundRes),
                    contentDescription = null
                  )
                  Text(
                    modifier = Modifier.padding(
                      MaterialTheme.spacing.extraSmall
                    ),
                    text = it.title.asString(context),
                    color = MaterialTheme.colorScheme.background
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

@Composable
private fun AssetListHeader(
  modifier: Modifier = Modifier,
  totalBalance: String
) {
  Column(
    modifier = modifier.padding(top = MaterialTheme.spacing.medium),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(modifier = Modifier) {
      Text(
        text = stringResource(id = R.string.wallet_asset__total_balance_big),
        modifier = Modifier.align(Alignment.CenterVertically),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primaryContainer
      )
      Icon(
        imageVector = Icons.Default.RemoveRedEye,
        contentDescription = null,
        modifier = Modifier.align(Alignment.CenterVertically),
        tint = MaterialTheme.colorScheme.primaryContainer
      )
    }
    Text(
      text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
          append("$ ")
        }
        withStyle(
          style = SpanStyle(
            color = MaterialTheme.colorScheme.primaryContainer,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight
          )
        ) {
          append(totalBalance)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
          append(" USD")
        }
      }
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
  }
}
