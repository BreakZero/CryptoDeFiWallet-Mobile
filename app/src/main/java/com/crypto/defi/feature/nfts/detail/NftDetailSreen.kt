package com.crypto.defi.feature.nfts.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NftDetailScreen(
  detailViewModel: DetailViewModel = hiltViewModel()
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      DeFiAppBar() {

      }
    }
  ) { paddings ->
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
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddings)
        .padding(MaterialTheme.Spacing.medium)
    ) {
      AndroidView(
        factory = { context ->
          PlayerView(context).also {
            it.player = detailViewModel.player
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
          .aspectRatio(16 / 9f)
      )
    }
  }
}