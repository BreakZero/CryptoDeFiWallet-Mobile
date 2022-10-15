@file:OptIn(ExperimentalMaterial3Api::class)

package com.crypto.defi.feature.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.ScannerView
import com.crypto.core.ui.utils.SetStatusColor

@Composable
fun DeFiScannerScreen(
  onResult: (String?) -> Unit
) {
  Scaffold(
    topBar = {
      DeFiAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = Color.Transparent
        )
      ) {
        onResult.invoke(null)
      }
    }
  ) {
    SetStatusColor(statusColor = Color.Transparent)
    ScannerView(onResult = onResult)
  }
}