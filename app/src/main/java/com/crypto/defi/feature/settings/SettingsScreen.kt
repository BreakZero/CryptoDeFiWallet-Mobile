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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.AdvanceMenu
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.MenuBlockView
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.resource.R

data class SettingItem(
  val icon: Int,
  val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  navigateUp: () -> Unit,
  navigateTo: (NavigationCommand) -> Unit
) {
  Scaffold(
    topBar = {
      DeFiAppBar() {
        navigateUp()
      }
    },
    modifier = Modifier.fillMaxSize(),
  ) {
    val scrollableState = rememberScrollState()

    Column(
      modifier = Modifier
        .padding(
          top = it.calculateTopPadding(),
          start = MaterialTheme.Spacing.medium,
          end = MaterialTheme.Spacing.medium
        )
        .verticalScroll(scrollableState)
    ) {
      Image(
        painter = painterResource(id = R.drawable.avatar_generic_1),
        contentDescription = null,
        modifier = Modifier
          .padding(top = MaterialTheme.Spacing.medium)
          .align(Alignment.CenterHorizontally)
          .size(MaterialTheme.Spacing.extraLarge)
      )
      Text(
        text = "Wallet Name",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = "Security",
        datas = listOf(
          AdvanceMenu(
            title = "Protect Your Wallet", subTitle = "Passcode, Biometrics and 2FA"
          ),
          AdvanceMenu(title = "Recovery Phrase", subTitle = "Wallet Name")
        )
      ) {}
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = "Account",
        datas = listOf(
          AdvanceMenu(title = "Display Currency", endValue = ""),
          AdvanceMenu(
            title = "Network Settings",
            endValue = ""
          )
        )
      ) {
        when (it) {
          0 -> {

          }
          1 -> {

          }
          else -> Unit
        }
      }
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = "Support",
        datas = listOf(
          AdvanceMenu(title = "Help Center"),
          AdvanceMenu(title = "New to DeFi"),
          AdvanceMenu(title = "Join Community"),
          AdvanceMenu(title = "Give Feedback")
        )
      ) {
      }
      MenuBlockView(
        modifier = Modifier.fillMaxWidth(), header = "About DeFiWallet",
        datas = listOf(
          AdvanceMenu(title = "Version", endValue = "v1.0.0", showIcon = false),
          AdvanceMenu(title = "Terms of Service"),
          AdvanceMenu(title = "Privacy Notice"),
          AdvanceMenu(title = "Visit our website")
        )
      ) {

      }
      Spacer(modifier = Modifier.height(MaterialTheme.Spacing.space24))
    }
  }
}