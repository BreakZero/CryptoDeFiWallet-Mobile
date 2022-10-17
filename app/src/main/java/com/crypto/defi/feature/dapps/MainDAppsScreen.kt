package com.crypto.defi.feature.dapps

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.LoadingIndicator
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.exceptions.rotating
import com.crypto.defi.feature.nfts.NFTContentPreview
import com.crypto.defi.navigations.DAppsNavigation
import com.crypto.defi.navigations.NftNavigation
import com.crypto.resource.R
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainDappsScreen(
  dAppsViewModel: MainDAppsViewModel = hiltViewModel(),
  navigateTo: (NavigationCommand) -> Unit
) {
  val dAppUiState by dAppsViewModel.dAppState.collectAsState()
  Scaffold(modifier = Modifier.fillMaxSize(),
    topBar = {
      SmallTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
          IconButton(onClick = {

          }) {
            dAppUiState.walletNameInfo.avator?.let {
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
                contentDescription = null
              )
            } ?: run {
              Image(
                modifier = Modifier
                  .size(MaterialTheme.Spacing.space48)
                  .clip(CircleShape)
                  .rotating(2500),
                painter = painterResource(id = dAppUiState.walletNameInfo.avatorRes),
                contentDescription = null
              )
            }
          }
        },
        title = {
          Column {
            Text(
              text = dAppUiState.walletNameInfo.walletName,
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
  ) {
    AnimatedContent(targetState = true, transitionSpec = {
      fadeIn(animationSpec = tween(300, 300)) with fadeOut(
        animationSpec = tween(
          300,
          300
        )
      )
    }) {
      if (dAppUiState.isLoading) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxSize()
        ) {
          LoadingIndicator(animating = true)
        }
      } else {
        if (dAppUiState.dApps.isEmpty()) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .fillMaxSize()
          ) {
            Text(modifier = Modifier.clickable {
              dAppsViewModel.onRefresh()
            }, text = "tap to refresh...", style = MaterialTheme.typography.bodyLarge)
          }
        } else {
          LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(MaterialTheme.Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small)
          ) {
            items(
              items = dAppUiState.dApps,
              key = {
                it.appName
              }
            ) { dApp ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    navigateTo(DAppsNavigation.detailDestination(dApp.url, dApp.rpc))
                  },
                elevation = CardDefaults.elevatedCardElevation(
                  defaultElevation = MaterialTheme.Spacing.extraSmall
                ),
                shape = RoundedCornerShape(MaterialTheme.Spacing.small)
              ) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth(),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  AsyncImage(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(MaterialTheme.Spacing.small))
                      .aspectRatio(1f),
                    model = ImageRequest.Builder(LocalContext.current)
                      .data(dApp.iconUrl)
                      .placeholder(R.drawable.avatar_generic_1)
                      .error(R.drawable.avatar_generic_1)
                      .crossfade(true)
                      .build(),
                    contentDescription = null
                  )
                  Text(text = dApp.appName, style = MaterialTheme.typography.bodyMedium)
                }
              }
            }
          }
        }
      }
    }
  }
}