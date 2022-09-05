package com.crypto.onboarding.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.crypto.onboarding.presentation.index.OnboardPager
import com.crypto.onboarding.presentation.legal.LegalPager
import com.crypto.onboarding.presentation.passcode.CreatePasscodePager
import com.crypto.onboarding.presentation.walletimport.ImportWordsPager
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.onboarding(navController: NavController) {
    composable(
        route = OnboardingNavigations.Index.destination,
        enterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(500))
        }
    ) {
        OnboardPager {
            navController.navigate(it.destination)
        }
    }

    composable(
        route = OnboardingNavigations.Legal.ROUTE,
        arguments = OnboardingNavigations.Legal.args,
        enterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(500))
        }
    ) {
        val forCreate =
            it.arguments?.getBoolean(OnboardingNavigations.KEY_IS_CREATE) ?: false
        LegalPager(
            forCreate = forCreate,
            navigateUp = { navController.navigateUp() },
            navigateTo = {
                navController.navigate(it.destination)
            }
        )
    }
    composable(
        route = OnboardingNavigations.CreatePasscode.ROUTE,
        arguments = OnboardingNavigations.CreatePasscode.args,
        enterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(500))
        }
    ) {
        val forCreate =
            it.arguments?.getBoolean(OnboardingNavigations.KEY_IS_CREATE)
                ?: false
        CreatePasscodePager(
            forCreate = forCreate,
            navigateUp = {
                navController.navigateUp()
            }, navigateTo = {
                navController.navigate(it.destination)
            })
    }
    composable(
        route = OnboardingNavigations.ImportWallet.ROUTE,
        arguments = OnboardingNavigations.ImportWallet.args,
        enterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(500))
        }
    ) {
        val passcode =
            it.arguments?.getString(OnboardingNavigations.KEY_PASSCODE)!!
        ImportWordsPager(
            passcode = passcode,
            navigateUp = {
                navController.navigateUp()
            }, navigateTo = {

            })
    }
}
