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

package com.easy.defi.app.feature.dapp.launch.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.app.feature.dapp.launch.DAppLaunchScreen
import org.jetbrains.annotations.VisibleForTesting

@VisibleForTesting
internal const val dAppChainIdArg = "dAppChainId"

@VisibleForTesting
internal const val dAppUrlArg = "dAppUrl"

@VisibleForTesting
internal const val dAppRpcArg = "dAppRpc"

internal class DAppLaunchArgs(val chainId: Int, val dAppUrl: String, val dAppRpc: String) {
  constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
    this(
      savedStateHandle[dAppChainIdArg] ?: -1,
      stringDecoder.decodeString(checkNotNull(savedStateHandle[dAppUrlArg])),
      stringDecoder.decodeString(checkNotNull(savedStateHandle[dAppRpcArg]))
    )
}

fun NavController.navigateIntoDApp(
  chainId: Int,
  dAppUrl: String,
  rpc: String
) {
  val encodedUrl = Uri.encode(dAppUrl)
  val encodedRpc = Uri.encode(rpc)
  this.navigate("app_launch_route?$dAppChainIdArg=$chainId&$dAppUrlArg=$encodedUrl&$dAppRpcArg=$encodedRpc")
}

fun NavGraphBuilder.dAppLaunchScreen(onBackClick: () -> Unit) {
  composableWithAnimation(
    route = "app_launch_route?$dAppChainIdArg={$dAppChainIdArg}&$dAppUrlArg={$dAppUrlArg}&$dAppRpcArg={$dAppRpcArg}",
    arguments = listOf(
      navArgument(dAppChainIdArg) { type = NavType.IntType },
      navArgument(dAppRpcArg) { type = NavType.StringType },
      navArgument(dAppRpcArg) { type = NavType.StringType }
    )
  ) {
    DAppLaunchScreen(onBackClick = onBackClick)
  }
}
