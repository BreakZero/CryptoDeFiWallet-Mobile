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

package com.easy.defi.app.feature.nft.detail.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.app.feature.nft.detail.NftDetailScreen

internal const val nftAssetContractAddressArg = "nft_contract"
internal const val nftAssetTokenIdArg = "nft_token_id"

internal class NftDetailArgs(
  val contractAddress: String,
  val tokenId: String
) {
  constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
    this(
      stringDecoder.decodeString(checkNotNull(savedStateHandle[nftAssetContractAddressArg])),
      stringDecoder.decodeString(checkNotNull(savedStateHandle[nftAssetTokenIdArg]))
    )
}

fun NavController.navigateToNftDetail(
  contractAddress: String,
  tokenId: String
) {
  val encodedContractAddress = Uri.encode(contractAddress)
  val encodedTokenId = Uri.encode(tokenId)
  this.navigate("nft_detail?$nftAssetContractAddressArg=$encodedContractAddress&$nftAssetTokenIdArg=$encodedTokenId")
}

fun NavGraphBuilder.nftDetailScreen(
  onBackClick: () -> Unit
) {
  composableWithAnimation(
    route = "nft_detail?$nftAssetContractAddressArg={$nftAssetContractAddressArg}&$nftAssetTokenIdArg={$nftAssetTokenIdArg}",
    arguments = listOf(
      navArgument(nftAssetContractAddressArg) { type = NavType.StringType },
      navArgument(nftAssetTokenIdArg) { type = NavType.StringType }
    )
  ) {
    NftDetailScreen(onBackClick = onBackClick)
  }
}
