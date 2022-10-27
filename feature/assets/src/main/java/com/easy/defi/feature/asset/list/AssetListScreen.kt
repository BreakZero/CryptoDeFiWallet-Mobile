@file:OptIn(ExperimentalFoundationApi::class)

package com.easy.defi.feature.asset.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.theme.Spacing
import com.easy.defi.app.core.ui.extension.rotating
import com.easy.defi.app.core.ui.navigation.NavigationCommand
import com.easy.defi.feature.asset.components.AssetCard
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalFoundationApi::class,
)
@Composable
fun AssetListScreen(
  assetsViewModel: AssetListViewModel = hiltViewModel(),
  navigateTo: (NavigationCommand) -> Unit,
) {
  val context = LocalContext.current
  val assetsUiState by assetsViewModel.assetState.collectAsState()
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
          IconButton(
            onClick = {
            },
          ) {
            assetsUiState.walletProfile.avator?.let {
              AsyncImage(
                modifier = Modifier
                  .size(MaterialTheme.Spacing.space48)
                  .clip(CircleShape)
                  .rotating(2500),
                model = ImageRequest.Builder(LocalContext.current)
                  .data(it)
                  .placeholder(R.drawable.avatar_generic_1)
                  .error(R.drawable.avatar_generic_1)
                  .crossfade(true)
                  .build(),
                contentDescription = null,
              )
            } ?: run {
              Image(
                modifier = Modifier
                  .size(MaterialTheme.Spacing.space48)
                  .clip(CircleShape)
                  .rotating(2500),
                painter = painterResource(id = R.drawable.avatar_generic_1),
                contentDescription = null,
              )
            }
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
            tint = MaterialTheme.colorScheme.primaryContainer,
          )
        },
        title = {
          Column {
            Text(
              text = assetsUiState.walletProfile.walletName,
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.primaryContainer,
            )
            Text(
              text = stringResource(id = R.string.avatar_wallet_layout__view_settings),
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.surfaceVariant,
            )
          }
        },
      )
    },
  ) {
    SwipeRefresh(
      modifier = Modifier
        .fillMaxSize()
        .padding(it),
      state = rememberSwipeRefreshState(assetsUiState.onRefreshing),
      swipeEnabled = true,
      onRefresh = {
        assetsViewModel.onRefresh()
      },
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .clip(
            RoundedCornerShape(
              topEnd = MaterialTheme.Spacing.space24,
              topStart = MaterialTheme.Spacing.space24,
            ),
          )
          .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
          vertical = MaterialTheme.Spacing.medium,
          horizontal = MaterialTheme.Spacing.small,
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small),
      ) {
        items(
          items = assetsUiState.assets,
          key = {
            it.slug
          },
        ) { asset ->
          AssetCard(
            modifier = Modifier
              .fillMaxWidth()
              .animateItemPlacement(),
            asset = asset,
          ) {
          }
        }
        item {
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
              MaterialTheme.Spacing.space12,
            ),
          ) {
            items(
              items = assetsUiState.promoCard,
              key = { promoCard ->
                promoCard.backgroundRes
              },
            ) {
              Card(
                modifier = Modifier
                  .width(MaterialTheme.Spacing.space128)
                  .aspectRatio(4 / 3f),
                elevation = CardDefaults.cardElevation(
                  defaultElevation = MaterialTheme.Spacing.extraSmall,
                ),
                shape = RoundedCornerShape(MaterialTheme.Spacing.small),
              ) {
                Box(
                  modifier = Modifier.fillMaxSize(),
                ) {
                  Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = it.backgroundRes),
                    contentDescription = null,
                  )
                  Text(
                    modifier = Modifier.padding(
                      MaterialTheme.Spacing.extraSmall,
                    ),
                    text = it.title.asString(context),
                    color = MaterialTheme.colorScheme.background,
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
