package com.crypto.defi.feature.nfts.detail

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  val player: ExoPlayer,
) : ViewModel() {
  companion object {
    private const val DEFAULT_MEDIA_URI = "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv"
  }

  init {
    val mediaItem = MediaItem.fromUri(DEFAULT_MEDIA_URI)
    player.setMediaItem(mediaItem)
  }

  override fun onCleared() {
    super.onCleared()
    player.release()
  }
}
