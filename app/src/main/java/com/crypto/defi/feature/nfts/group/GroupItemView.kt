package com.crypto.defi.feature.nfts.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crypto.core.ui.Spacing
import com.crypto.defi.feature.nfts.NFTContentPreview
import com.crypto.defi.models.remote.nft.NftAssetGroup
import com.crypto.resource.R

@Composable
fun GroupItemView(
  modifier: Modifier = Modifier, group: NftAssetGroup
) {
  Card(
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.Spacing.space12)
    ) {
      Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
          modifier = Modifier
            .size(MaterialTheme.Spacing.space48)
            .clip(CircleShape), model = ImageRequest.Builder(LocalContext.current).data(group.logoUrl).placeholder(R.drawable.avatar_generic_1).error(R.drawable.avatar_generic_1).crossfade(true).build(), contentDescription = null
        )
        Column(
          modifier = Modifier
            .height(MaterialTheme.Spacing.space48)
            .padding(
              start = MaterialTheme.Spacing.small, top = MaterialTheme.Spacing.extraSmall, bottom = MaterialTheme.Spacing.extraSmall
            ), verticalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = group.contractName)
          Text(
            text = group.contractAddress, maxLines = 1, overflow = TextOverflow.Ellipsis
          )
        }
      }
      Spacer(modifier = Modifier.height(MaterialTheme.Spacing.small))
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.space12)
      ) {
        items(
          items = group.assets,
          key = {
            it.tokenId
          }
        ) { asset ->
          NFTContentPreview(
            modifier = Modifier
              .size(100.dp)
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