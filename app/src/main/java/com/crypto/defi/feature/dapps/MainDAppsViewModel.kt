package com.crypto.defi.feature.dapps

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.models.domain.AppSettingsConfig
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.dapps.DAppInfoDto
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDAppsViewModel @Inject constructor(
  private val client: HttpClient,
  appSettingsConfig: DataStore<AppSettingsConfig>
) : ViewModel() {

  private val _isLoading = MutableStateFlow(true)
  private val _dAppsFlow = MutableStateFlow(emptyList<DAppInfo>())

  init {
    viewModelScope.launch {
      _dAppsFlow.update {
        loadDApps()
      }
    }
  }

  val dAppState = combine(_isLoading, _dAppsFlow, appSettingsConfig.data) { isLoading, dApps, appSettings ->
    MainDAppState(walletNameInfo = appSettings.walletNameInfo, isLoading = isLoading, dApps = dApps)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MainDAppState())


  private suspend fun loadDApps(): List<DAppInfo> {
    _isLoading.update { true }
    val dApps = try {
      client.get("${UrlConstant.BASE_URL}/dapps").body<BaseResponse<List<DAppInfoDto>>>().data
        .filterNot {
          it.rpc.isBlank()
        }.map {
          DAppInfo(
            chainId = it.chainId.toIntOrNull() ?: 1,
            iconUrl = it.icon,
            url = it.url,
            appName = it.name,
            rpc = it.rpc
          )
        }
    } catch (e: Exception) {
      emptyList<DAppInfo>()
    }
    _isLoading.update { false }
    return dApps
  }

  fun onRefresh() {
    viewModelScope.launch {
      _dAppsFlow.update {
        loadDApps()
      }
    }
  }
}