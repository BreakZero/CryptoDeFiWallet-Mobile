package com.crypto.defi.chains.evm

import androidx.annotation.Keep
import com.crypto.core.extensions._16toNumber
import com.crypto.core.extensions.clearHexPrefix
import com.crypto.core.extensions.hexStringToByteArray
import com.crypto.core.extensions.orElse
import com.crypto.core.extensions.toHexByteArray
import com.crypto.core.extensions.toHexString
import com.crypto.defi.chains.IChain
import com.crypto.defi.common.UrlConstant
import com.crypto.defi.exceptions.InsufficientBalanceException
import com.crypto.defi.feature.assets.send.ReadyToSign
import com.crypto.defi.feature.assets.send.TransactionPlan
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.mapper.toEvmTransaction
import com.crypto.defi.models.remote.BaseResponse
import com.crypto.defi.models.remote.EvmTransactionDto
import com.crypto.defi.models.remote.FeeHistoryDto
import com.google.protobuf.ByteString
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.math.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.proto.Ethereum

class EvmChainImpl(
  private val httpClient: HttpClient,
  private val hdWallet: HDWallet,
) : IChain {
  override fun address(): String {
    return hdWallet.getAddressForCoin(CoinType.ETHEREUM)
  }

  override suspend fun balance(contract: String?): BigInteger {
    return try {
      val response = httpClient.get("${UrlConstant.BASE_URL}/ethereum/balance/${address()}") {
        contract?.also {
          parameter("contract", it)
        }
      }.body<BaseResponse<String>>()
      response.data.toBigInteger()
    } catch (e: Exception) {
      BigInteger.ZERO
    }
  }

  override suspend fun transactions(
    page: Int,
    offset: Int,
    contract: String?,
  ): List<EvmTransaction> {
    return try {
      httpClient.get(
        urlString = "${UrlConstant.BASE_URL}/ethereum/transactions/${address()}",
      ) {
        parameter("page", page)
        parameter("offset", 20)
        if (!contract.isNullOrEmpty()) {
          parameter("contract", contract)
        }
      }.body<BaseResponse<List<EvmTransactionDto>>>().data.map {
        it.toEvmTransaction(address())
      }
    } catch (e: Exception) {
      emptyList()
    }
  }

  override suspend fun signTransaction(readyToSign: ReadyToSign): TransactionPlan {
    return withContext(Dispatchers.IO) {
      val balance = balance(readyToSign.contract)
      if (balance < readyToSign.amount) throw InsufficientBalanceException()
      val nonce = fetchNonce()
      val (baseFee, priorityFee) = feeHistory()
      val gasLimit = estimateGasLimit(
        from = address(),
        to = readyToSign.contract.orElse(readyToSign.to),
        input = readyToSign.contract?.let {
          "0x70a08231000000000000000000000000${address().clearHexPrefix()}"
        },
      )
      val prvKey =
        ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
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
        Ethereum.SigningOutput.parser(),
      )
      TransactionPlan(
        rawData = output.encoded.toByteArray().toHexString(),
        action = "ETH Transfer",
        amount = readyToSign.amount,
        to = readyToSign.to,
        from = address(),
        fee = gasLimit.toBigInteger().times(baseFee),
      )
    }
  }

  override suspend fun broadcast(rawData: String) = withContext(Dispatchers.IO) {
    httpClient.post {
      url("${UrlConstant.BASE_URL}/ethereum/transaction/broadcast")
      setBody(rawData)
    }.body<BaseResponse<String>>().data
  }

  private suspend fun estimateGasLimit(
    chain: String = "ethereum",
    from: String,
    to: String,
    input: String? = null,
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

  private suspend fun fetchNonce(
    chain: String = "ethereum",
  ) = withContext(Dispatchers.IO) {
    httpClient.get("${UrlConstant.BASE_URL}/$chain/${address()}/nonce")
      .body<BaseResponse<Long>>().data
  }

  private suspend fun feeHistory(
    chain: String = "ethereum",
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
          ?: emptyList(),
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
  val priorityFeePerGas: List<BigInteger>,
)
