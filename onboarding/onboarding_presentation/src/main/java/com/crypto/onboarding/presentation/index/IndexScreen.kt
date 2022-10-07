package com.crypto.onboarding.presentation.index

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.core.ui.utils.setStatusColor
import com.crypto.onboarding.presentation.OnboardingNavigations
import com.crypto.resource.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

private val banners = listOf(
    OnboardBannerInfo(
        imageRes = R.drawable.banner_wallet,
        title = R.string.welcome_intro_adapter__welcome,
        message = R.string.welcome_intro_adapter__welcome_tip
    ),
    OnboardBannerInfo(
        imageRes = R.drawable.banner_secure,
        title = R.string.welcome_intro_adapter__secure,
        message = R.string.welcome_intro_adapter__secure_tip
    ),
    OnboardBannerInfo(
        imageRes = R.drawable.banner_flexable,
        title = R.string.welcome_intro_adapter__flexible,
        message = R.string.welcome_intro_adapter__flexible_tip
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardScreen(
    navigateTo: (NavigationCommand) -> Unit
) {
  setStatusColor(statusColor = MaterialTheme.colorScheme.surface)
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.Spacing.medium),
        verticalArrangement = Arrangement.Center
    ) {
      val pagerState = rememberPagerState()
      val bannerState = remember {
        banners
      }
      Image(
          modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = MaterialTheme.Spacing.medium)
              .align(Alignment.CenterHorizontally),
          contentScale = ContentScale.FillWidth,
          painter = painterResource(id = R.drawable.img_defi_header),
          contentDescription = null
      )
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
              .padding(MaterialTheme.Spacing.medium),
      )
      Button(
          modifier = Modifier
              .fillMaxWidth(),
          enabled = false, // do not support right now
          onClick = {
            // do not support now.
            // navigateTo(OnboardingNavigations.Legal.destination(true))
          }) {
        Text(stringResource(id = R.string.welcome__create_a_new_wallet))
      }
      Button(
          modifier = Modifier
              .fillMaxWidth(),
          onClick = {
            navigateTo(OnboardingNavigations.Legal.destination(false))
          }) {
        Text(stringResource(id = R.string.welcome__importing_an_existing_wallet))
      }
    }
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