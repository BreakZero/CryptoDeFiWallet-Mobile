package com.easy.defi.app.feature.nft.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.common.result.Result
import com.easy.defi.app.core.common.result.asResult
import com.easy.defi.app.core.data.repository.nft.NftRepository
import com.easy.defi.app.core.model.data.NftInfo
import com.easy.defi.app.feature.nft.detail.navigation.NftDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NftDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  stringDecoder: StringDecoder,
  nftRepository: NftRepository,
  val player: ExoPlayer
) : ViewModel() {
  private val nftDetailArgs: NftDetailArgs = NftDetailArgs(savedStateHandle, stringDecoder)

  companion object {
    private const val DEFAULT_MEDIA_URI = "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv"
  }

  init {
    val mediaItem = MediaItem.fromUri(DEFAULT_MEDIA_URI)
    player.setMediaItem(mediaItem)
  }

  val nftDetailState = nftRepository.getNftAssetByTokenId(
    contractAddress = nftDetailArgs.contractAddress,
    tokenId = nftDetailArgs.tokenId
  ).asResult().map { nftResult ->
    Timber.tag("=====").v(nftResult.toString())
    when (nftResult) {
      is Result.Loading -> NftDetailState.Loading
      is Result.Error -> NftDetailState.Error
      is Result.Success<NftInfo> -> NftDetailState.Success(nftResult.data)
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), NftDetailState.Loading)

  override fun onCleared() {
    super.onCleared()
    player.release()
  }
}

sealed interface NftDetailState {
  object Loading : NftDetailState
  object Error : NftDetailState
  data class Success(val nftInfo: NftInfo) : NftDetailState
}
