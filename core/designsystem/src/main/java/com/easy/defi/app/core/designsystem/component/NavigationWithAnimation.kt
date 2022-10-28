package com.easy.defi.app.core.designsystem.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableWithAnimation(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  deepLinks: List<NavDeepLink> = emptyList(),
  content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
  composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
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
    },
    content = content
  )
}
