package com.crypto.onboarding.presentation

import com.crypto.core.ui.routers.ParameterType
import com.crypto.core.ui.routers.buildNavigator
import com.crypto.core.ui.routers.parameter

object OnboardingRouter {
    const val KEY_OF_LEGAL = "is_create"

    val Index = buildNavigator(ParameterType.NONE, "onboarding_index")

    fun Legal(forCreate: Boolean = false) =
        buildNavigator(ParameterType.OPTIONAL, "onboarding_legal") {
            parameter {
                KEY_OF_LEGAL to forCreate
            }
        }

    fun CreatePassCode(forCreate: Boolean = false) =
        buildNavigator(ParameterType.OPTIONAL, "onboarding_create_passcode") {
            parameter {
                KEY_OF_LEGAL to forCreate
            }
        }
}