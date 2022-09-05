package com.crypto.defi.feature.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.navigations.MainNavigation
import com.crypto.onboarding.presentation.OnboardingNavigations
import com.crypto.resource.R

@Composable
fun SplashPager(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigateTo: (NavigationCommand) -> Unit
) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    // AnimationEffect
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }
            )
        )
        splashViewModel.uiEvent.collect {
            navigateTo.invoke(
                if (it) MainNavigation.Main else OnboardingNavigations.Index
            )
        }
    }

    // Image
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_wallet_logo),
            contentDescription = null,
            modifier = Modifier
                .scale(scale.value)
                .fillMaxWidth()
        )
    }
}
