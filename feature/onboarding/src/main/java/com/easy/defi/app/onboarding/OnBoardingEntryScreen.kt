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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.ui.navigation.NavigationCommand
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

private val banners = listOf(
  OnboardBannerInfo(
    imageRes = R.drawable.banner_wallet,
    title = R.string.welcome_intro__welcome,
    message = R.string.welcome_intro__welcome_tip
  ),
  OnboardBannerInfo(
    imageRes = R.drawable.banner_secure,
    title = R.string.welcome_intro__secure,
    message = R.string.welcome_intro__secure_tip
  ),
  OnboardBannerInfo(
    imageRes = R.drawable.banner_flexable,
    title = R.string.welcome_intro__flexible,
    message = R.string.welcome_intro__flexible_tip
  )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardScreen(
  navigateTo: (NavigationCommand) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(MaterialTheme.spacing.medium),
    verticalArrangement = Arrangement.Center
  ) {
    val pagerState = rememberPagerState()
    val bannerState = remember {
      banners
    }
    HorizontalPager(
      count = bannerState.size,
      state = pagerState,
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.CenterHorizontally)
        .weight(1.0F)
    ) { page ->
      OnboardBanner(info = bannerState[page], modifier = Modifier.fillMaxSize())
    }
    HorizontalPagerIndicator(
      pagerState = pagerState,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(MaterialTheme.spacing.medium)
    )
    Button(
      modifier = Modifier
        .fillMaxWidth(),
      enabled = false, // do not support right now
      onClick = {
        // do not support now.
        // navigateTo(OnboardingNavigations.Legal.destination(true))
      }
    ) {
      Text(stringResource(id = R.string.welcome__create_a_new_wallet))
    }
    Button(
      modifier = Modifier
        .fillMaxWidth(),
      onClick = {
        navigateTo(OnBoardingNavigations.Legal.destination(false))
      }
    ) {
      Text(stringResource(id = R.string.welcome__importing_an_existing_wallet))
    }
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
  }
}

@Composable
private fun OnboardBanner(
  info: OnboardBannerInfo,
  modifier: Modifier
) {
  Box(modifier = modifier, contentAlignment = Alignment.Center) {
    Column {
      Image(
        painterResource(id = info.imageRes),
        contentDescription = null,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = stringResource(id = info.title),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = stringResource(id = info.message),
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.secondary
      )
    }
  }
}

private data class OnboardBannerInfo(
  @DrawableRes val imageRes: Int,
  @StringRes val title: Int,
  @StringRes val message: Int
)
