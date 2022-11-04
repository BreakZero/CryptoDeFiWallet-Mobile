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

package com.easy.defi.app.core.data.repository.chain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.easy.defi.app.core.common.exception.InsufficientBalanceException
import com.easy.defi.app.core.common.extensions.clearHexPrefix
import com.easy.defi.app.core.common.extensions.hexStringToByteArray
import com.easy.defi.app.core.common.extensions.orElse
import com.easy.defi.app.core.common.extensions.toHexByteArray
import com.easy.defi.app.core.common.extensions.toHexString
import com.easy.defi.app.core.data.HdWalletHolder
import com.easy.defi.app.core.data.Synchronizer
import com.easy.defi.app.core.data.paging.TransactionPagingSource
import com.easy.defi.app.core.database.dao.AssetDao
import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.model.data.ReadyToSign
import com.easy.defi.app.core.model.data.TransactionPlan
import com.easy.defi.app.core.network.EthereumDataSource
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger
import javax.inject.Inject

class EthereumRepository @Inject constructor(
  private val hdWalletHolder: HdWalletHolder,
  private val ethereumDataSource: EthereumDataSource,
  private val assetDao: AssetDao
) : ChainRepository {

  override val address: String?
    get() = hdWalletHolder.hdWallet?.getAddressForCoin(CoinType.ETHEREUM)

  override suspend fun getBalance(contractAddress: String?): BigInteger {
    return address?.let {
      ethereumDataSource.getSingleBalance(it, contractAddress)
    } ?: BigInteger.ZERO
  }

  override suspend fun getTransactions(
    contractAddress: String?
  ): Flow<PagingData<BaseTransaction>> {
    return Pager(PagingConfig(pageSize = 20)) {
      TransactionPagingSource(
        contractAddress = contractAddress,
        address = address,
        ethereumDataSource = ethereumDataSource
      )
    }.flow
  }

  override suspend fun broadcastTransaction(rawData: String): String {
    return ethereumDataSource.broadcast(rawData)
  }

  override suspend fun sign(readyToSign: ReadyToSign): TransactionPlan {
    return address?.let {
      withContext(Dispatchers.IO) {
        val balance = getBalance(readyToSign.contract)
        if (balance < readyToSign.amount) throw InsufficientBalanceException()
        val nonce = ethereumDataSource.fetchNonce(it)
        val (baseFee, priorityFee) = ethereumDataSource.feeHistory()
        val gasLimit = ethereumDataSource.estimateGasLimit(
          from = it,
          to = readyToSign.contract.orElse(readyToSign.to),
          input = readyToSign.contract?.let {
            "0x70a08231000000000000000000000000${it.clearHexPrefix()}"
          }
        )
        val prvKey =
          ByteString.copyFrom(hdWalletHolder.hdWallet?.getKeyForCoin(CoinType.ETHEREUM)?.data())
        val signer = readyToSign.contract?.let {
          val tokenTransfer = Ethereum.Transaction.ERC20Transfer.newBuilder().apply {
            to = readyToSign.to
            amount = ByteString.copyFrom(readyToSign.amount.toHexByteArray())
          }
          Ethereum.SigningInput.newBuilder().apply {
            this.privateKey = prvKey
            this.toAddress = it
            this.chainId = ByteString.copyFrom(readyToSign.chainId.toHexByteArray())
            this.nonce = ByteString.copyFrom(nonce.toHexByteArray())
            this.txMode = Ethereum.TransactionMode.Enveloped
            this.maxFeePerGas = ByteString.copyFrom(baseFee.toHexByteArray())
            this.maxInclusionFeePerGas =
              ByteString.copyFrom(priorityFee.toHexByteArray())
            this.gasLimit = ByteString.copyFrom(gasLimit.toBigInteger().toHexByteArray())
            this.transaction = Ethereum.Transaction.newBuilder().apply {
              erc20Transfer = tokenTransfer.build()
            }.build()
          }
        } ?: kotlin.run {
          val transfer = Ethereum.Transaction.Transfer.newBuilder().apply {
            this.amount = ByteString.copyFrom(readyToSign.amount.toHexByteArray())
            readyToSign.memo?.also {
              this.data = ByteString.copyFrom(it.hexStringToByteArray())
            }
          }
          Ethereum.SigningInput.newBuilder().apply {
            this.privateKey = prvKey
            this.toAddress = readyToSign.to
            this.chainId = ByteString.copyFrom(readyToSign.chainId.toHexByteArray())
            this.nonce = ByteString.copyFrom(nonce.toHexByteArray())
            this.txMode = Ethereum.TransactionMode.Enveloped
            this.maxFeePerGas = ByteString.copyFrom(baseFee.toHexByteArray())
            this.maxInclusionFeePerGas = ByteString.copyFrom(priorityFee.toHexByteArray())
            this.gasLimit = ByteString.copyFrom(gasLimit.toBigInteger().toHexByteArray())
            this.transaction = Ethereum.Transaction.newBuilder().apply {
              this.transfer = transfer.build()
            }.build()
          }
        }
        val output = AnySigner.sign(
          signer.build(),
          CoinType.ETHEREUM,
          Ethereum.SigningOutput.parser()
        )
        TransactionPlan(
          rawData = output.encoded.toByteArray().toHexString(),
          action = "ETH Transfer",
          amount = readyToSign.amount,
          to = readyToSign.to,
          from = it,
          fee = gasLimit.toBigInteger().times(baseFee)
        )
      }
    } ?: throw IllegalAccessException("address is empty")
  }

  override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
    return address?.let {
      val ethBalance = ethereumDataSource.getSingleBalance(it, null)
      assetDao.updateBalanceForMainChain("eth", ethBalance.toString())
      val tokenHoldings = ethereumDataSource.getTokenHoldings(it)
      tokenHoldings.onEach {
        assetDao.updateBalance(contract = it.contractAddress, balance = it.amount)
      }
      true
    } ?: false
  }
}
