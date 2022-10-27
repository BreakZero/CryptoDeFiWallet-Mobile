package com.easy.defi.app.core.model.data

import java.math.BigInteger

class EvmTransaction(
  hash: String,
  direction: TransactionDirection,
  from: String,
  to: String,
  timeStamp: String,
  value: BigInteger,
  input: String,
  gas: BigInteger,
  gasPrice: BigInteger,
) : BaseTransaction(
  hash,
  direction,
  from,
  to,
  value,
  timeStamp,
)
