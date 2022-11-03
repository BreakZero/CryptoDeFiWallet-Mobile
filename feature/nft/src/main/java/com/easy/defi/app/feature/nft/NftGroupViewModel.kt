package com.easy.defi.app.feature.nft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.common.result.Result
import com.easy.defi.app.core.common.result.ResultLoadState
import com.easy.defi.app.core.common.result.asResult
import com.easy.defi.app.core.data.repository.nft.NftRepository
import com.easy.defi.app.core.data.repository.user.UserDataRepository
import com.easy.defi.app.core.model.data.NftGroup
import com.easy.defi.app.core.model.data.WalletProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NftListViewModel @Inject constructor(
  userDataRepository: UserDataRepository,
  nftRepository: NftRepository
) : ViewModel() {
  private val _userProfile = userDataRepository.userDataStream
  private val _nftGroup = nftRepository.getGroupByErcType("erc721").asResult()

  val nftGroupState = combine(_userProfile, _nftGroup) { userData, nftGroupResult ->
    when (nftGroupResult) {
      Result.Loading -> {
        NftGroupState(walletProfile = userData.walletProfile, loadState = ResultLoadState.Loading)
      }
      is Result.Error -> {
        NftGroupState(walletProfile = userData.walletProfile, loadState = ResultLoadState.Error)
      }
      is Result.Success -> {
        NftGroupState(
          walletProfile = userData.walletProfile,
          loadState = ResultLoadState.Success,
          nftGroups = nftGroupResult.data
        )
      }
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, NftGroupState())
}

data class NftGroupState(
  val walletProfile: WalletProfile = WalletProfile("", ""),
  val loadState: ResultLoadState = ResultLoadState.Loading,
  val nftGroups: List<NftGroup> = emptyList()
)
