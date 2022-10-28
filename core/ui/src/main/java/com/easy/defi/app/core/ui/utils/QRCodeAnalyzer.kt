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

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QRCodeAnalyzer(
  private val onCodeScanned: (result: String) -> Unit,
) : ImageAnalysis.Analyzer {
  private val supportedImageFormats = listOf(
    ImageFormat.YUV_420_888,
    ImageFormat.YUV_422_888,
    ImageFormat.YUV_444_888
  )
  private var finished = false

  override fun analyze(image: ImageProxy) {
    if (image.format in supportedImageFormats) {
      val bytes = image.planes.first().buffer.toByteArray()
      val source = PlanarYUVLuminanceSource(
        bytes,
        image.width,
        image.height,
        0,
        0,
        image.width,
        image.height,
        false
      )
      val binaryBmp = BinaryBitmap(HybridBinarizer(source))
      try {
        val result = MultiFormatReader().apply {
          setHints(
            mapOf(
              DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                BarcodeFormat.QR_CODE
              )
            )
          )
        }.decode(binaryBmp)
        if (result.text.isNotBlank() && !finished) {
          onCodeScanned(result.text)
          finished = true
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        image.close()
      }
    }
  }

  private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    return ByteArray(remaining()).also {
      get(it)
    }
  }
}
