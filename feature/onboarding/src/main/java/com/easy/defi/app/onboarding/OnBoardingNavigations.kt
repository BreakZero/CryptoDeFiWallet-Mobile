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

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.defi.app.core.ui.navigation.NavigationCommand

object OnBoardingNavigations {
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
      forCreate: Boolean,
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
      forCreate: Boolean,
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
      passcode: String,
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
