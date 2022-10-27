package com.easy.defi.app.onboarding.wallet.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSetupScreen(
  navigateUp: () -> Unit,
) {
  Scaffold(
    topBar = {
      DeFiAppBar() {
        navigateUp()
      }
    },
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      Text(text = stringResource(id = R.string.wallet_protect__protect_wallet))
      Text(text = stringResource(id = R.string.wallet_protect__wallet_protect_tips))
      Image(painter = painterResource(id = R.drawable.banner_secure), contentDescription = null)
      Card(
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.fillMaxHeight()) {
        }
      }
    }
  }
}