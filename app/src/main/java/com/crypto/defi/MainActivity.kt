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
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.dialog
import com.crypto.core.ui.composables.NormalTipsView
import com.crypto.core.ui.models.NormalTips
import com.crypto.defi.feature.main.MainPager
import com.crypto.defi.feature.splash.SplashPager
import com.crypto.defi.navigations.MainNavigation
import com.crypto.defi.navigations.SplashNavigation
import com.crypto.defi.ui.theme.DeFiWalletTheme
import com.crypto.onboarding.presentation.onboarding
import com.crypto.resource.R
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
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = SplashNavigation.Splashing.destination,
                        modifier = Modifier
                    ) {
                        composable(
                            SplashNavigation.Splashing.destination,
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
                            SplashPager {
                                navController.navigate(it.destination) {
                                    popUpTo(SplashNavigation.Splashing.destination) {
                                        inclusive = true
                                    }
                                }
                            }
                        }

                        composable(
                            MainNavigation.Main.destination,
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
                            MainPager {
                                navController.navigate(it.destination)
                            }
                        }

                        onboarding(navController)
                        /**
                         * common ui
                         */
                        dialog("normal_tips") {
                            NormalTipsView(tips = NormalTips(
                                title = stringResource(id = R.string.two_fa_view__2_factor_authentication),
                                message = stringResource(id = R.string.wallet_protect__2fa_desc),
                                iconRes = R.drawable.ic_shell
                            ))
                        }
                    }
                }
            }
        }
    }
}
