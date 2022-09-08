package com.crypto.defi.navigations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object TransactionListNavigation {
    const val KEY_CODE = "asset-code"
    const val ROUTE = "transaction_list?${KEY_CODE}={${KEY_CODE}}"
    val args = listOf(
        navArgument(KEY_CODE) { type = NavType.StringType }
    )
    fun destination(
        code: String
    ) = object : NavigationCommand {
        override val arguments
            get() = args
        override val destination = "transaction_list?${KEY_CODE}=$code"
    }
}