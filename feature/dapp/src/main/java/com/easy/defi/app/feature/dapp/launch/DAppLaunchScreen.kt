package com.easy.defi.app.feature.dapp.launch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun DAppLaunchScreen(
  dAppLaunchViewModel: DAppLaunchViewModel = hiltViewModel()
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val provideJs by remember {
    mutableStateOf(
      context.resources.openRawResource(1).bufferedReader().use {
        it.readText()
      }
    )
  }
}
