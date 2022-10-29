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
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.ui.DeFiAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSetupScreen(
  navigateUp: () -> Unit
) {
  Scaffold(
    topBar = {
      DeFiAppBar() {
        navigateUp()
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      Text(text = stringResource(id = R.string.wallet_protect__protect_wallet))
      Text(text = stringResource(id = R.string.wallet_protect__wallet_protect_tips))
      Image(painter = painterResource(id = R.drawable.banner_secure), contentDescription = null)
      Card(
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.fillMaxHeight()) {
        }
      }
    }
  }
}
