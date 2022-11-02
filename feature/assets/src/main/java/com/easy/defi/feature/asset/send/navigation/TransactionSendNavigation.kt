package com.easy.defi.feature.asset.send.navigation

import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.feature.asset.send.SendFormScreen

@VisibleForTesting
internal const val transactionSendSlugArg = "transaction_send_slug_arg"

internal class TransactionSendArgs(val slug: String) {
  constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
    this(stringDecoder.decodeString(checkNotNull(savedStateHandle[transactionSendSlugArg])))
}

fun NavController.navigateToSend(slug: String) {
  val encodedSlug = Uri.encode(slug)
  this.navigate("transaction_send_route/$encodedSlug")
}

fun NavGraphBuilder.transactionSendScreen(
  onBackClick: () -> Unit
) {
  composableWithAnimation(
    route = "transaction_send_route/{$transactionSendSlugArg}",
    arguments = listOf(
      navArgument(transactionSendSlugArg) { type = NavType.StringType }
    )
  ) {
    SendFormScreen(onBackClick = onBackClick)
  }
}
