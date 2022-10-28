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

package com.easy.defi.app.core.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.easy.defi.app.core.ui.utils.QRCodeAnalyzer

@Composable
fun ScannerView(
  onResult: (String) -> Unit,
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val cameraProviderFuture = remember {
    ProcessCameraProvider.getInstance(context)
  }
  var hasCamPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
      ) == PackageManager.PERMISSION_GRANTED
    )
  }
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { granted ->
      hasCamPermission = granted
    }
  )
  LaunchedEffect(key1 = true) {
    launcher.launch(Manifest.permission.CAMERA)
  }
  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    if (hasCamPermission) {
      AndroidView(
        factory = { context ->
          val previewView = PreviewView(context)
          val preview = Preview.Builder().build()
          val selector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
          preview.setSurfaceProvider(previewView.surfaceProvider)
          val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(480, 480))
            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            .build()
          imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            QRCodeAnalyzer { result ->
              if (result.isNotEmpty()) onResult.invoke(result)
            }
          )
          kotlin.runCatching {
            cameraProviderFuture.get().bindToLifecycle(
              lifecycleOwner,
              selector,
              preview,
              imageAnalysis
            )
          }.onFailure {
            it.printStackTrace()
          }
          previewView
        },
        modifier = Modifier.fillMaxSize()
      )
      Canvas(modifier = Modifier.fillMaxSize()) {
        val startX = size.width / 4.0f
        val startY = size.height / 4.0f
        drawRect(
          color = Color.Red,
          topLeft = Offset(startX, startY),
          size = androidx.compose.ui.geometry.Size(
            width = size.width / 2.0f,
            height = size.width / 2.0f
          ),
          style = Stroke(width = 2.0f)
        )
      }
    }
  }
}
