package com.crypto.defi.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.AdvanceMenu
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.MenuBlockView
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.core.ui.utils.SetStatusColor
import com.crypto.defi.BuildConfig
import com.crypto.defi.exceptions.rotating
import com.crypto.defi.navigations.SettingsNavigation
import com.crypto.defi.navigations.WebViewNavigation
import com.crypto.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  settingsViewModel: SettingsViewModel = hiltViewModel(),
  popBack: () -> Unit,
  navigateTo: (NavigationCommand) -> Unit
) {
  Scaffold(
    topBar = {
      DeFiAppBar(
        title = stringResource(id = R.string.settings__title)
      ) {
        popBack()
      }
    },
    modifier = Modifier.fillMaxSize(),
  ) {
    val scrollableState = rememberScrollState()
    val settingsUiState by settingsViewModel.settingsState.collectAsState()
    Column(
      modifier = Modifier
        .padding(
          top = it.calculateTopPadding(),
          start = MaterialTheme.Spacing.medium,
          end = MaterialTheme.Spacing.medium
        )
        .verticalScroll(scrollableState)
    ) {
      settingsUiState.walletNameInfo.avator?.let { avatorUrl ->
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(avatorUrl)
            .placeholder(R.drawable.avatar_generic_1)
            .error(R.drawable.avatar_generic_1)
            .crossfade(true)
            .build(),
          contentDescription = null,
          modifier = Modifier
            .padding(top = MaterialTheme.Spacing.medium)
            .clip(CircleShape)
            .align(Alignment.CenterHorizontally)
            .size(MaterialTheme.Spacing.space128)
            .rotating(2500)
        )
      } ?: kotlin.run {
        Image(
          painter = painterResource(id = R.drawable.avatar_generic_1),
          contentDescription = null,
          modifier = Modifier
            .padding(top = MaterialTheme.Spacing.medium)
            .clip(CircleShape)
            .align(Alignment.CenterHorizontally)
            .size(MaterialTheme.Spacing.space128)
        )
      }

      Text(
        text = settingsUiState.walletNameInfo.walletName,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = stringResource(id = R.string.settings__security),
        datas = listOf(
          AdvanceMenu(
            title = stringResource(id = R.string.settings__protect_your_wallet),
            subTitle = stringResource(id = R.string.settings__passcode_biometrics_and_2fa)
          ),
          AdvanceMenu(
            title = stringResource(id = R.string.settings__recovery_phrase),
            subTitle = settingsUiState.walletNameInfo.walletName
          )
        )
      ) {

      }
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = stringResource(id = R.string.settings__account),
        datas = listOf(
          AdvanceMenu(
            title = stringResource(id = R.string.settings__display_currency),
            endValue = "${settingsUiState.currency.code}(${settingsUiState.currency.symbol})"
          ),
          AdvanceMenu(
            title = stringResource(id = R.string.settings__network_settings),
            endValue = settingsUiState.network.label
          )
        )
      ) {
        when (it) {
          0 -> {
            navigateTo(SettingsNavigation.Settings_Currency)
          }
          1 -> {
            navigateTo(SettingsNavigation.Settings_Chain)
          }
          else -> Unit
        }
      }
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = stringResource(id = R.string.settings__support),
        datas = listOf(
          AdvanceMenu(title = stringResource(id = R.string.settings__help_center)),
          AdvanceMenu(title = stringResource(id = R.string.settings__new_to_defi)),
          AdvanceMenu(title = stringResource(id = R.string.settings__join_community)),
          AdvanceMenu(title = stringResource(id = R.string.settings__give_feedback))
        )
      ) {
      }
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = stringResource(id = R.string.settings__about_crypto_com_wallet),
        datas = listOf(
          AdvanceMenu(title = stringResource(id = R.string.settings__version), endValue = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})", showIcon = false),
          AdvanceMenu(title = stringResource(id = R.string.settings__terms_of_service)),
          AdvanceMenu(title = stringResource(id = R.string.settings__privacy_notice)),
          AdvanceMenu(title = stringResource(id = R.string.settings__visit_our_website))
        )
      ) { itemIndex ->
        when (itemIndex) {
          0 -> {
            settingsViewModel.updateWalletName()
          }
          3 -> {
            navigateTo(WebViewNavigation.visitWebsite("https://github.com/BreakZero"))
          }
          else -> Unit
        }
      }
      Spacer(modifier = Modifier.height(MaterialTheme.Spacing.space24))
    }
  }
}