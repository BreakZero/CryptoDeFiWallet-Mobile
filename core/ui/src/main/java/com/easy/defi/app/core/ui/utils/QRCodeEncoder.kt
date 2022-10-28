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

package com.easy.defi.app.core.ui.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeEncoder {
  fun encodeQRCode(
    content: String,
    width: Int = 500,
    height: Int = 500,
  ): Bitmap? {
    val qrCodeWriter = QRCodeWriter()
    val hints = hashMapOf<EncodeHintType, String>().apply {
      put(EncodeHintType.CHARACTER_SET, "utf-8")
    }
    val encode = kotlin.runCatching {
      qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
    }.onFailure {
      it.printStackTrace()
    }.getOrNull()

    return encode?.let {
      val colors = IntArray(width * height)
      (0 until width).forEach { wi ->
        (0 until height).forEach { hi ->
          val index = wi * width + hi
          if (it.get(wi, hi)) {
            colors[index] = if (index % 2 == 0) Color.BLUE else Color.GREEN
          } else {
            colors[index] = Color.WHITE
          }
        }
      }
      Bitmap.createBitmap(colors, width, height, Bitmap.Config.RGB_565)
    }
  }
}
