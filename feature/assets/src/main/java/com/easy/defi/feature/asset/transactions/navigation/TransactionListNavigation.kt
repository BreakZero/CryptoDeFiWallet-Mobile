package com.easy.defi.feature.asset.transactions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.feature.asset.transactions.TransactionListScreen

const val assetTransactionListRoute = "transaction_list_route"

fun NavController.toTransactionList(navOptions: NavOptions? = null) {
  this.navigate(assetTransactionListRoute, navOptions)
}

fun NavGraphBuilder.transactionListScreen(
  onBackClick: () -> Unit
) {
  composableWithAnimation(route = assetTransactionListRoute) {
    TransactionListScreen(onBackClick = onBackClick)
  }
}
