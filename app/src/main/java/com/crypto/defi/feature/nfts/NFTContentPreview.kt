package com.crypto.defi.feature.nfts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crypto.core.ui.Spacing
import com.crypto.defi.models.remote.nft.NftInfo
import com.crypto.resource.R

@Composable
fun NFTContentPreview(
  modifier: Modifier = Modifier,
  nftInfo: NftInfo
) {
  when {
    nftInfo.contentType?.startsWith("video/") == true -> {
      Image(
        imageVector = Icons.Default.MovieFilter,
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
        contentDescription = null
      )
    }
    nftInfo.contentType?.startsWith("audio/") == true -> {
      Image(
        imageVector = Icons.Default.AudioFile,
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
        contentDescription = null
      )
    }
    else -> {
      AsyncImage(
        modifier = modifier,
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