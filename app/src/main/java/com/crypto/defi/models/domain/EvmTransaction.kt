package com.crypto.defi.models.domain

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
  gasPrice: BigInteger
) : BaseTransaction(
  hash, direction, from, to, value, timeStamp
)
