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

package com.easy.defi.feature.asset.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.x.byDecimal

@ExperimentalMaterial3Api
@Composable
fun AssetCard(
  modifier: Modifier = Modifier,
  asset: Asset,
  onClick: (Asset) -> Unit
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(
      defaultElevation = MaterialTheme.spacing.extraSmall
    ),
    shape = RoundedCornerShape(MaterialTheme.spacing.space12),
    onClick = {
      onClick(asset)
    }
  ) {
    Row(
      modifier = modifier
        .padding(MaterialTheme.spacing.small)
        .fillMaxWidth()
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        modifier = Modifier
          .size(MaterialTheme.spacing.space48)
          .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
          .data(asset.iconUrl)
          .placeholder(R.drawable.avatar_generic_1)
          .error(R.drawable.avatar_generic_1)
          .crossfade(true)
          .build(),
        contentScale = ContentScale.Fit,
        contentDescription = null
      )
      Text(
        text = asset.name,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .weight(1f)
          .padding(start = MaterialTheme.spacing.small)
      )
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .wrapContentWidth(Alignment.End),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = "${asset.nativeBalance.byDecimal(asset.decimal)} ${asset.symbol}",
          textAlign = TextAlign.End
        )
        Text(text = "$ ${asset.fiatBalance().toPlainString()}", textAlign = TextAlign.End)
      }
    }
  }
}
