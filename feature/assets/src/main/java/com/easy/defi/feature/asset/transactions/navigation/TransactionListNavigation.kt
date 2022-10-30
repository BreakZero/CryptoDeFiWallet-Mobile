package com.easy.defi.feature.asset.transactions.navigation

import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.defi.app.core.common.decoder.StringDecoder
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.feature.asset.transactions.TransactionListScreen

@VisibleForTesting
internal const val assetSlugArg = "assetSlug"

internal class TransactionListArgs(val slug: String) {
  constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
    this(stringDecoder.decodeString(checkNotNull(savedStateHandle[assetSlugArg])))
}

fun NavController.toTransactionList(slug: String) {
  val encodedSlug = Uri.encode(slug)
  this.navigate("transaction_list_route/$encodedSlug")
}

fun NavGraphBuilder.transactionListScreen(
  onBackClick: () -> Unit
) {
  composableWithAnimation(
    route = "transaction_list_route/{$assetSlugArg}",
    arguments = listOf(
      navArgument(assetSlugArg) { type = NavType.StringType }
    )
  ) {
    TransactionListScreen(onBackClick = onBackClick)
  }
}
