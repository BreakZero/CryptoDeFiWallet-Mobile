package com.crypto.defi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crypto.defi.ui.theme.DeFiWalletTheme
import com.crypto.onboarding.presentation.OnboardingRouter
import com.crypto.onboarding.presentation.index.OnboardPager
import com.crypto.onboarding.presentation.legal.LegalPager
import com.crypto.onboarding.presentation.passcode.CreatePasscodePager
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DeFiWalletTheme {
                val systemUIController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                SideEffect {
                    systemUIController.setStatusBarColor(
                        Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                val navController = rememberAnimatedNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = OnboardingRouter.Index.route(),
                        modifier = Modifier
                    ) {
                        composable(
                            route = OnboardingRouter.Index.route(),
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
                                navController.navigate(it.router())
                            }
                        }

                        composable(
                            route = OnboardingRouter.Legal().route(),
                            arguments = listOf(
                                navArgument(OnboardingRouter.KEY_OF_LEGAL) {
                                    type = NavType.BoolType
                                }
                            ),
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
                                it.arguments?.getBoolean(OnboardingRouter.KEY_OF_LEGAL) ?: false
                            LegalPager(
                                forCreate = forCreate,
                                navigateUp = { navController.navigateUp() },
                                navigateTo = {
                                    navController.navigate(it.router())
                                }
                            )
                        }
                        composable(
                            route = OnboardingRouter.CreatePassCode().route(),
                            arguments = listOf(
                                navArgument(OnboardingRouter.KEY_OF_LEGAL) {
                                    type = NavType.BoolType
                                }
                            ),
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
                                it.arguments?.getBoolean(OnboardingRouter.KEY_OF_LEGAL) ?: false
                            CreatePasscodePager(navigateUp = {
                                navController.navigateUp()
                            }, navigateTo = {

                            })
                        }
                    }
                }
            }
        }
    }
}
