/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi.app.core.network

import com.easy.defi.app.core.model.data.EvmTransaction
import com.easy.defi.app.core.model.data.TokenHolding
import com.easy.defi.app.core.network.model.BaseResponse
import com.easy.defi.app.core.network.model.EvmTransactionDto
import com.easy.defi.app.core.network.model.TokenHoldingDto
import com.easy.defi.app.core.network.model.asExternalModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import timber.log.Timber
import java.math.BigInteger
import javax.inject.Inject

class EthereumDataSource @Inject constructor(
  private val httpClient: HttpClient
) {
  suspend fun getTokenHoldings(
    address: String
  ): List<TokenHolding> {
    return try {
      val result: BaseResponse<List<TokenHoldingDto>> =
        httpClient.get("${UrlConstant.BASE_URL}/ethereum/$address/tokenholdings").body()

      result.data.map {
        TokenHolding(
          amount = it.amount,
          contractAddress = it.contractAddress,
          contractName = it.contractName
        )
      }
    } catch (e: Exception) {
      Timber.e(e)
      emptyList()
    }
  }

  suspend fun getSingleBalance(
    address: String,
    contractAddress: String?
  ): BigInteger {
    return try {
      val response = httpClient.get("${UrlConstant.BASE_URL}/ethereum/balance/$address") {
        contractAddress?.also {
          parameter("contract", it)
        }
      }.body<BaseResponse<String>>()
      response.data.toBigInteger()
    } catch (e: Exception) {
      BigInteger.ZERO
    }
  }

  suspend fun getTransactions(
    address: String,
    page: Int,
    offset: Int,
    contract: String?
  ): List<EvmTransaction> {
    return try {
      httpClient.get(
        urlString = "${UrlConstant.BASE_URL}/ethereum/transactions/$address"
      ) {
        parameter("page", page)
        parameter("offset", offset)
        if (!contract.isNullOrEmpty()) {
          parameter("contract", contract)
        }
      }.body<BaseResponse<List<EvmTransactionDto>>>().data.map {
        it.asExternalModel(address)
      }
    } catch (e: Exception) {
      emptyList()
    }
  }

  suspend fun broadcast(rawData: String): String {
    return httpClient.post {
      url("${UrlConstant.BASE_URL}/ethereum/transaction/broadcast")
      setBody(rawData)
    }.body<BaseResponse<String>>().data
  }
}
