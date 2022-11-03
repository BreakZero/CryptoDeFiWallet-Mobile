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
import javax.inject.Inject

@HiltViewModel
class NftDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  stringDecoder: StringDecoder,
  nftRepository: NftRepository,
  val player: ExoPlayer
) : ViewModel() {
  private val nftDetailArgs: NftDetailArgs = NftDetailArgs(savedStateHandle, stringDecoder)

  val nftDetailState = nftRepository.getNftAssetByTokenId(
    contractAddress = nftDetailArgs.contractAddress,
    tokenId = nftDetailArgs.tokenId
  ).asResult().map { nftResult ->
    when (nftResult) {
      is Result.Loading -> NftDetailUiState.Loading
      is Result.Error -> NftDetailUiState.Error
      is Result.Success<NftInfo> -> {
        val nftInfo = nftResult.data
        if (nftInfo.contentType?.startsWith("video/") == true) {
          nftInfo.contentUri?.let {
            val mediaItem = MediaItem.fromUri(it)
            player.setMediaItem(mediaItem)
          }
        }
        NftDetailUiState.Success(nftResult.data)
      }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), NftDetailUiState.Loading)

  override fun onCleared() {
    super.onCleared()
    player.release()
  }
}

sealed interface NftDetailUiState {
  object Loading : NftDetailUiState
  object Error : NftDetailUiState
  data class Success(val nftInfo: NftInfo) : NftDetailUiState
}
