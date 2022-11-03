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

package com.easy.defi.app.feature.nft

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.MovieFilter
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easy.defi.app.core.common.result.ResultLoadState
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.component.brushBackground
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.model.data.NftGroup
import com.easy.defi.app.core.model.data.NftInfo
import com.easy.defi.app.core.ui.LoadingIndicator
import com.easy.defi.app.core.ui.extension.rotating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NftGroupScreen(
  nftGroupViewModel: NftListViewModel = hiltViewModel(),
  navigateToSettings: () -> Unit,
  navigateToNftDetail: (String, String) -> Unit
) {
  val nftGroupUiState by nftGroupViewModel.nftGroupState.collectAsState()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .brushBackground(
        listOf(
          MaterialTheme.colorScheme.primary,
          MaterialTheme.colorScheme.surface,
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
          nftGroupUiState.walletProfile.avator?.let {
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
            text = nftGroupUiState.walletProfile.walletName,
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
    when (nftGroupUiState.loadState) {
      ResultLoadState.Loading -> {
        Box(
          modifier = Modifier
            .fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          LoadingIndicator(animating = true)
        }
      }
      ResultLoadState.Error -> {
        Box(
          modifier = Modifier
            .fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "somethings went wrong")
        }
      }
      ResultLoadState.Success -> {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(MaterialTheme.spacing.medium),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
          items(
            items = nftGroupUiState.nftGroups,
            key = {
              it.contractAddress
            }
          ) { group ->
            GroupItemView(group = group) {
              navigateToNftDetail(it.contractAddress, it.tokenId)
            }
          }
        }
      }
    }
  }
}

@Composable
internal fun LazyItemScope.GroupItemView(
  modifier: Modifier = Modifier,
  group: NftGroup,
  onItemClick: (NftInfo) -> Unit
) {
  Card(
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.spacing.space12)
    ) {
      Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
          modifier = Modifier
            .size(MaterialTheme.spacing.space48)
            .clip(CircleShape),
          model = ImageRequest.Builder(LocalContext.current)
            .data(group.logoUrl)
            .placeholder(R.drawable.avatar_generic_1)
            .error(
              R.drawable.avatar_generic_1
            )
            .crossfade(true)
            .build(),
          contentDescription = null
        )
        Column(
          modifier = Modifier
            .height(MaterialTheme.spacing.space48)
            .padding(
              start = MaterialTheme.spacing.small,
              top = MaterialTheme.spacing.extraSmall,
              bottom = MaterialTheme.spacing.extraSmall
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
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space12)
      ) {
        items(
          items = group.assets,
          key = {
            "${it.contractAddress}-${it.tokenId}"
          }
        ) { asset ->
          NFTContentPreview(
            modifier = Modifier
              .size(100.dp)
              .aspectRatio(1.0f)
              .clip(RoundedCornerShape(MaterialTheme.spacing.space24))
              .background(color = MaterialTheme.colorScheme.surface),
            nftInfo = asset,
            onItemClick = onItemClick
          )
        }
      }
    }
  }
}

@Composable
internal fun NFTContentPreview(
  modifier: Modifier = Modifier,
  onItemClick: (NftInfo) -> Unit,
  nftInfo: NftInfo
) {
  when {
    nftInfo.contentType?.startsWith("video/") == true -> {
      Image(
        imageVector = Icons.Default.MovieFilter,
        modifier = modifier.clickable {
          onItemClick(nftInfo)
        },
        contentScale = ContentScale.FillWidth,
        contentDescription = null
      )
    }
    nftInfo.contentType?.startsWith("audio/") == true -> {
      Image(
        imageVector = Icons.Default.AudioFile,
        modifier = modifier.clickable {
          onItemClick(nftInfo)
        },
        contentScale = ContentScale.FillWidth,
        contentDescription = null
      )
    }
    else -> {
      AsyncImage(
        modifier = modifier.clickable {
          onItemClick(nftInfo)
        },
        contentScale = ContentScale.Crop,
        model = ImageRequest.Builder(LocalContext.current)
          .data(nftInfo.nftscanUri ?: nftInfo.imageUri)
          .placeholder(R.drawable.avatar_generic_1)
          .error(R.drawable.avatar_generic_1)
          .crossfade(true)
          .build(),
        contentDescription = null
      )
    }
  }
}
