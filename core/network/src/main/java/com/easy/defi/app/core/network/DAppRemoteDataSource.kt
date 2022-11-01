package com.easy.defi.app.core.network

import com.easy.defi.app.core.model.data.DApp
import com.easy.defi.app.core.network.model.BaseResponse
import com.easy.defi.app.core.network.model.dapps.DAppInfoDto
import com.easy.defi.app.core.network.model.dapps.asExternalModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class DAppRemoteDataSource @Inject constructor(
  private val httpClient: HttpClient
) {
  suspend fun loadAllDApps(): List<DApp> {
    return httpClient.get("${UrlConstant.BASE_URL}/dapps")
      .body<BaseResponse<List<DAppInfoDto>>>().data.map {
        it.asExternalModel()
      }
  }
}
