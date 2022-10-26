package com.crypto.defi.feature.assets.components

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
import com.crypto.core.extensions.byDecimal
import com.crypto.core.ui.Spacing
import com.crypto.defi.models.domain.Asset
import com.crypto.resource.R

@ExperimentalMaterial3Api
@Composable
fun AssetCard(
  modifier: Modifier = Modifier,
  asset: Asset,
  onClick: (Asset) -> Unit,
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(
      defaultElevation = MaterialTheme.Spacing.extraSmall,
    ),
    shape = RoundedCornerShape(MaterialTheme.Spacing.space12),
    onClick = {
      onClick(asset)
    },
  ) {
    Row(
      modifier = modifier
        .padding(MaterialTheme.Spacing.small)
        .fillMaxWidth()
        .height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      AsyncImage(
        modifier = Modifier
          .size(MaterialTheme.Spacing.space48)
          .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
          .data(asset.iconUrl)
          .placeholder(R.drawable.avatar_generic_1)
          .error(R.drawable.avatar_generic_1)
          .crossfade(true)
          .build(),
        contentScale = ContentScale.Fit,
        contentDescription = null,
      )
      Text(
        text = asset.name,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .weight(1f)
          .padding(start = MaterialTheme.Spacing.small),
      )
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .wrapContentWidth(Alignment.End),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center,
      ) {
        Text(text = "${asset.nativeBalance.byDecimal(asset.decimal)} ${asset.symbol}", textAlign = TextAlign.End)
        Text(text = "$ ${asset.fiatBalance().toPlainString()}", textAlign = TextAlign.End)
      }
    }
  }
}
