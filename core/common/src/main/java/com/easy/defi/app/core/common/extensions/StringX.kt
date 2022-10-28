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

package com.easy.defi.app.core.common.extensions

import java.math.BigInteger

private const val HEX_PREFIX = "0x"
fun String?.orElse(default: String): String {
  return this ?: default
}

fun String.clearHexPrefix(): String {
  return if (startsWith(HEX_PREFIX)) {
    substring(2)
  } else {
    this
  }
}

fun String._16toNumber(): BigInteger {
  return this.clearHexPrefix().toBigInteger(16)
}

fun String.mark(
  size: Int,
  markChar: String = "***",
  dir: MarkDir = MarkDir.MIDDLE,
): String {
  when (dir) {
    MarkDir.MIDDLE -> {
      return if (length <= size * 2) {
        this
      } else {
        StringBuilder()
          .append(take(size))
          .append(markChar)
          .append(takeLast(size))
          .toString()
      }
    }
    MarkDir.START -> {
      return if (length <= size) {
        this
      } else {
        StringBuilder()
          .append(markChar)
          .append(takeLast(size))
          .toString()
      }
    }
    MarkDir.END -> {
      return if (length <= size) {
        this
      } else {
        StringBuilder()
          .append(take(size))
          .append(markChar)
          .toString()
      }
    }
  }
}

enum class MarkDir {
  START, MIDDLE, END
}
