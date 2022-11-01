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

package com.easy.defi.app.feature.dapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easy.defi.app.core.common.result.ResultLoadState
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.component.brushBackground
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.ui.LoadingIndicator
import com.easy.defi.app.core.ui.extension.rotating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DAppListScreen(
  dAppListViewModel: DAppListViewModel = hiltViewModel()
) {
  val uiState by dAppListViewModel.uiState.collectAsState()
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

        }) {
          uiState.walletProfile.avator?.let {
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
            text = uiState.walletProfile.walletName,
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
    when (uiState.loadState) {
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
        LazyVerticalGrid(
          columns = GridCells.Fixed(3),
          contentPadding = PaddingValues(MaterialTheme.spacing.medium),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
          items(
            uiState.dApps,
            key = {
              it.url
            }) { dApp ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = MaterialTheme.spacing.extraSmall,
              ),
              shape = RoundedCornerShape(MaterialTheme.spacing.small)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(MaterialTheme.spacing.extraSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                AsyncImage(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .aspectRatio(1f),
                  model = ImageRequest.Builder(LocalContext.current)
                    .data(dApp.icon)
                    .placeholder(R.drawable.avatar_generic_1)
                    .error(R.drawable.avatar_generic_1)
                    .crossfade(true)
                    .build(),
                  contentDescription = null,
                )
                Text(
                  text = dApp.name,
                  style = MaterialTheme.typography.bodyMedium,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
          }
        }
      }
    }
  }
}
