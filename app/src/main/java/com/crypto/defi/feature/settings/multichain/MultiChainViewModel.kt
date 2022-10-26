package com.crypto.defi.feature.settings.multichain

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.models.domain.AppSettingsConfig
import com.crypto.defi.models.domain.ChainNetwork
import com.crypto.defi.models.domain.SUPPORT_NETWORKS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MultiChainViewModel @Inject constructor(
  private val appSettingsConfig: DataStore<AppSettingsConfig>,
) : ViewModel() {
  val multiChainState = appSettingsConfig.data.map {
    MultiChainState(
      selected = it.network,
      supports = SUPPORT_NETWORKS,
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MultiChainState())

  fun update(network: ChainNetwork) {
    viewModelScope.launch {
      appSettingsConfig.updateData {
        it.copy(
          network = network,
        )
      }
    }
  }
}
