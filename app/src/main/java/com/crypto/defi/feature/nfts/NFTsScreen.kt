package com.crypto.defi.feature.nfts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.LoadingIndicator
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.navigations.NftNavigation
import com.crypto.resource.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainNFTsScreen(
  nftsViewModel: NFTsViewModel = hiltViewModel(),
  navigateTo: (NavigationCommand) -> Unit
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
  ) { _ ->
    val nftAssetsUiState by nftsViewModel.ownerAssetState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "Your NFTs")
        Button(onClick = {
          navigateTo(NftNavigation.groupDestination)
        }) {
          Text(text = "All")
        }
      }
      AnimatedContent(targetState = true, transitionSpec = {
        fadeIn(animationSpec = tween(300, 300)) with fadeOut(
          animationSpec = tween(
            300,
            300
          )
        )
      }) {
        if (nftAssetsUiState.isLoading) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .fillMaxSize()
          ) {
            LoadingIndicator(animating = true)
          }
        } else {
          LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(MaterialTheme.Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small)
          ) {
            items(nftAssetsUiState.nfts) { asset ->
              Button(onClick = {
                navigateTo(NftNavigation.detailDestination("mock-nft"))
              }) {
                NFTContentPreview(
                  modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
                    .clip(RoundedCornerShape(MaterialTheme.Spacing.space24))
                    .background(color = MaterialTheme.colorScheme.surface),
                  nftInfo = asset
                )
              }
            }
          }
        }
      }
    }
  }
}