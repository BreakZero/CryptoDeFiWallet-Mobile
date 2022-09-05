package com.crypto.onboarding.presentation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.core.ui.routers.NavigationCommand

object OnboardingNavigations {
    const val KEY_IS_CREATE = "is_create"
    const val KEY_PASSCODE = "passcode"

    val Index = object : NavigationCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = "onboarding_index"

    }

    object Legal {
        const val ROUTE = "onboarding_legal?$KEY_IS_CREATE={$KEY_IS_CREATE}"
        val args = listOf(
            navArgument(KEY_IS_CREATE) { type = NavType.BoolType }
        )

        fun destination(
            forCreate: Boolean
        ) = object : NavigationCommand {
            override val arguments
                get() = args
            override val destination = "onboarding_legal?$KEY_IS_CREATE=$forCreate"
        }
    }

    object CreatePasscode {
        const val ROUTE = "onboarding_create_passcode?$KEY_IS_CREATE={$KEY_IS_CREATE}"
        val args = listOf(
            navArgument(KEY_IS_CREATE) { type = NavType.BoolType }
        )
        fun destination(
            forCreate: Boolean
        ) = object : NavigationCommand {
            override val arguments
                get() = args
            override val destination = "onboarding_create_passcode?$KEY_IS_CREATE=$forCreate"
        }
    }

    object ImportWallet {
        const val ROUTE = "onboarding_import_wallet?$KEY_PASSCODE={$KEY_PASSCODE}"
        val args = listOf(
            navArgument(KEY_PASSCODE) { type = NavType.StringType }
        )
        fun destination(
            passcode: String
        ) = object : NavigationCommand {
            override val arguments
                get() = args
            override val destination = "onboarding_import_wallet?$KEY_PASSCODE=$passcode"
        }
    }

    /*fun CreatePassCode(forCreate: Boolean = false) =
        buildNavigator(ParameterType.OPTIONAL, "onboarding_create_passcode") {
            parameter {
                KEY_OF_LEGAL to forCreate
            }
        }*/
}