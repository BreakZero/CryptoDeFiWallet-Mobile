package com.easy.defi.app.feature.nft.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.ui.DeFiAppBar

@Composable
internal fun NftDetailScreen(
  nftDetailViewModel: NftDetailViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {

  var lifecycle by remember {
    mutableStateOf(Lifecycle.Event.ON_CREATE)
  }
  val lifecycleOwner = LocalLifecycleOwner.current
  DisposableEffect(key1 = lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event -> lifecycle = event }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  Column(modifier = Modifier.fillMaxSize()) {
    DeFiAppBar() {
      onBackClick()
    }
    AndroidView(
      factory = { context ->
        PlayerView(context).also {
          it.player = nftDetailViewModel.player
        }
      },
      update = {
        when (lifecycle) {
          Lifecycle.Event.ON_PAUSE -> {
            it.onPause()
            it.player?.pause()
          }
          Lifecycle.Event.ON_RESUME -> {
            it.onResume()
          }
          else -> Unit
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.spacing.medium)
        .aspectRatio(16 / 9f)
    )
  }
}
