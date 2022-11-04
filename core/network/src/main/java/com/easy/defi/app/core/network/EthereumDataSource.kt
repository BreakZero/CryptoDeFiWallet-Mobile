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

import androidx.annotation.Keep
import com.easy.defi.app.core.common.extensions._16toNumber
import com.easy.defi.app.core.model.data.EvmTransaction
import com.easy.defi.app.core.model.data.TokenHolding
import com.easy.defi.app.core.network.model.BaseResponse
import com.easy.defi.app.core.network.model.EvmTransactionDto
import com.easy.defi.app.core.network.model.FeeHistoryDto
import com.easy.defi.app.core.network.model.TokenHoldingDto
import com.easy.defi.app.core.network.model.asExternalModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    return httpClient.get(
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
  }

  suspend fun broadcast(rawData: String): String {
    return httpClient.post {
      url("${UrlConstant.BASE_URL}/ethereum/transaction/broadcast")
      setBody(rawData)
    }.body<BaseResponse<String>>().data
  }

  suspend fun fetchNonce(
    address: String,
    chain: String = "ethereum"
  ) = withContext(Dispatchers.IO) {
    httpClient.get("${UrlConstant.BASE_URL}/$chain/$address/nonce")
      .body<BaseResponse<Long>>().data
  }

  suspend fun estimateGasLimit(
    chain: String = "ethereum",
    from: String,
    to: String,
    input: String? = null
  ) = withContext(Dispatchers.IO) {
    return@withContext input?.let {
      val response = httpClient.get("${UrlConstant.BASE_URL}/$chain/estimateGas") {
        parameter("from", from)
        parameter("to", to)
        parameter("input_data", it)
      }.body<BaseResponse<Long>>()
      response.data
    } ?: 21000L
  }

  suspend fun feeHistory(
    chain: String = "ethereum"
  ) = withContext(Dispatchers.Default) {
    val history = httpClient.get("${UrlConstant.BASE_URL}/$chain/feeHistory")
      .body<BaseResponse<FeeHistoryDto>>()
    val baseFee = formatFeeHistory(history.data)
    Pair(baseFee, baseFee)
  }

  private fun formatFeeHistory(historyDto: FeeHistoryDto): BigInteger {
    val oldestBlock = historyDto.oldestBlock
    val blocks = historyDto.baseFeePerGas.mapIndexed { index, value ->
      BlockInfo(
        number = oldestBlock._16toNumber().plus(index.toBigInteger()),
        baseFeePerGas = value._16toNumber(),
        gasUsedRatio = historyDto.gasUsedRatio.getOrNull(index) ?: 0.0,
        priorityFeePerGas = historyDto.reward.getOrNull(index)?.map { it._16toNumber() }
          ?: emptyList()
      )
    }
    val firstPercentialPriorityFees = blocks.first().priorityFeePerGas
    val sum = firstPercentialPriorityFees.reduce { acc, bigInteger -> acc.plus(bigInteger) }
    val manual = sum.divide(firstPercentialPriorityFees.size.toBigInteger())
    return manual
  }
}

@Keep
internal data class BlockInfo(
  val number: BigInteger,
  val baseFeePerGas: BigInteger,
  val gasUsedRatio: Double,
  val priorityFeePerGas: List<BigInteger>
)
