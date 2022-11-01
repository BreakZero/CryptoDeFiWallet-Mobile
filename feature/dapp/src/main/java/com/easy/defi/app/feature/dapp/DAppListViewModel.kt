package com.easy.defi.app.feature.dapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.defi.app.core.common.result.Result
import com.easy.defi.app.core.common.result.ResultLoadState
import com.easy.defi.app.core.common.result.asResult
import com.easy.defi.app.core.common.utils.DeFiConstant
import com.easy.defi.app.core.data.repository.dapp.DAppRepository
import com.easy.defi.app.core.data.repository.user.OfflineUserDataRepository
import com.easy.defi.app.core.model.data.DApp
import com.easy.defi.app.core.model.data.WalletProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DAppListViewModel @Inject constructor(
  offlineUserDataRepository: OfflineUserDataRepository,
  dAppRepository: DAppRepository
) : ViewModel() {

  private val _walletFlow = offlineUserDataRepository.userDataStream.map {
    it.walletProfile
  }
  private val _dappsFlow = dAppRepository.getRecommendDApps().asResult()

  val uiState = combine(_walletFlow, _dappsFlow) { walletProfile, dAppResult ->
    when (dAppResult) {
      is Result.Loading -> DAppListUiState(
        walletProfile = walletProfile,
        loadState = ResultLoadState.Loading
      )
      is Result.Error -> DAppListUiState(
        walletProfile = walletProfile,
        loadState = ResultLoadState.Error
      )
      is Result.Success -> {
        DAppListUiState(
          walletProfile = walletProfile,
          loadState = ResultLoadState.Success,
          dApps = dAppResult.data.filter { it.chainId.isNotBlank() }
        )
      }
    }
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
    DAppListUiState(loadState = ResultLoadState.Loading)
  )
}

data class DAppListUiState(
  val walletProfile: WalletProfile = WalletProfile(null, DeFiConstant.DEFAULT_WALLET_NAME),
  val dApps: List<DApp> = emptyList(),
  val loadState: ResultLoadState
)
