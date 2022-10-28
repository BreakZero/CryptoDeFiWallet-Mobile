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

package com.easy.defi.app.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.easy.defi.app.core.designsystem.component.composableWithAnimation
import com.easy.defi.app.onboarding.legal.LegalScreen
import com.easy.defi.app.onboarding.passcode.CreatePasscodeScreen
import com.easy.defi.app.onboarding.wallet.imports.ImportWordsScreen

fun NavGraphBuilder.onBoardingGraph(
  navController: NavController,
) {
  composableWithAnimation(
    route = OnBoardingNavigations.Index.destination
  ) {
    OnboardScreen {
      navController.navigate(it.destination)
    }
  }

  composableWithAnimation(
    route = OnBoardingNavigations.Legal.ROUTE,
    arguments = OnBoardingNavigations.Legal.args
  ) {
    val forCreate =
      it.arguments?.getBoolean(OnBoardingNavigations.KEY_IS_CREATE) ?: false
    LegalScreen(
      forCreate = forCreate,
      navigateUp = { navController.navigateUp() },
      navigateTo = {
        navController.navigate(it.destination)
      }
    )
  }
  composableWithAnimation(
    route = OnBoardingNavigations.CreatePasscode.ROUTE,
    arguments = OnBoardingNavigations.CreatePasscode.args
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
      }
    )
  }
  composableWithAnimation(
    route = OnBoardingNavigations.ImportWallet.ROUTE,
    arguments = OnBoardingNavigations.ImportWallet.args
  ) {
    val passcode = it.arguments?.getString(OnBoardingNavigations.KEY_PASSCODE).orEmpty()
    ImportWordsScreen(
      passcode = passcode,
      navigateUp = {
        navController.navigateUp()
      }
    )
  }
}
