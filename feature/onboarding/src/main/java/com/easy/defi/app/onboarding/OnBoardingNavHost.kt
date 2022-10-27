package com.easy.defi.app.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.easy.defi.app.onboarding.legal.LegalScreen
import com.easy.defi.app.onboarding.passcode.CreatePasscodeScreen
import com.easy.defi.app.onboarding.wallet.imports.ImportWordsScreen

fun NavGraphBuilder.onboarding(navController: NavController) {
  composable(
    route = OnBoardingNavigations.Index.destination,
  ) {
    OnboardScreen {
      navController.navigate(it.destination)
    }
  }

  composable(
    route = OnBoardingNavigations.Legal.ROUTE,
    arguments = OnBoardingNavigations.Legal.args,
  ) {
    val forCreate =
      it.arguments?.getBoolean(OnBoardingNavigations.KEY_IS_CREATE) ?: false
    LegalScreen(
      forCreate = forCreate,
      navigateUp = { navController.navigateUp() },
      navigateTo = {
        navController.navigate(it.destination)
      },
    )
  }
  composable(
    route = OnBoardingNavigations.CreatePasscode.ROUTE,
    arguments = OnBoardingNavigations.CreatePasscode.args,
  ) {
    val forCreate =
      it.arguments?.getBoolean(OnBoardingNavigations.KEY_IS_CREATE)
        ?: false
    CreatePasscodeScreen(
      forCreate = forCreate,
      navigateUp = {
        navController.navigateUp()
      },
      navigateTo = {
        navController.navigate(it.destination)
      },
    )
  }
  composable(
    route = OnBoardingNavigations.ImportWallet.ROUTE,
    arguments = OnBoardingNavigations.ImportWallet.args,
  ) {
    val passcode = it.arguments?.getString(OnBoardingNavigations.KEY_PASSCODE).orEmpty()
    ImportWordsScreen(
      passcode = passcode,
      navigateUp = {
        navController.navigateUp()
      },
      navigateMain = {
        navController.navigate("main-home") {
          popUpTo(OnBoardingNavigations.Index.destination) {
            inclusive = true
          }
        }
      },
    )
  }
}
