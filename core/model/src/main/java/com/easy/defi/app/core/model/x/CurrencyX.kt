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

package com.easy.defi.app.core.model.x

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun BigInteger.byDecimal2String(
  decimal: Int,
  display: Int = 8,
): String {
  return byDecimal(decimal, display)
    .toPlainString()
}

fun BigInteger.byDecimal(
  decimal: Int,
  display: Int = 8,
): BigDecimal {
  return this.toBigDecimal()
    .movePointLeft(decimal)
    .setScale(display, RoundingMode.DOWN)
}

fun BigDecimal.upWithDecimal(
  decimal: Int,
): BigInteger {
  return this.movePointRight(decimal).toBigInteger()
}
