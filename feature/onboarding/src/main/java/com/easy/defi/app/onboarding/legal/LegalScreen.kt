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

package com.easy.defi.app.onboarding.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.ui.AdvanceMenu
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.core.ui.MenuItemView
import com.easy.defi.app.core.ui.navigation.NavigationCommand
import com.easy.defi.app.onboarding.OnBoardingNavigations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreen(
  forCreate: Boolean,
  navigateUp: () -> Unit,
  navigateTo: (NavigationCommand) -> Unit
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      DeFiAppBar() {
        navigateUp()
      }
    }
  ) {
    Column(
      modifier = Modifier.padding(it)
    ) {
      Text(
        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
        text = stringResource(id = R.string.legal__legal),
        style = MaterialTheme.typography.titleLarge
      )
      Text(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
        text = stringResource(
          id = R.string.legal__legal_tips
        )
      )
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.spacing.medium),
        shape = RoundedCornerShape(8.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          MenuItemView(
            modifier = Modifier.fillMaxWidth(),
            data = AdvanceMenu(title = stringResource(id = R.string.legal__terms_of_service))
          ) {
          }
          Divider()
          MenuItemView(
            modifier = Modifier.fillMaxWidth(),
            data = AdvanceMenu(title = stringResource(id = R.string.legal__privacy_notice))
          ) {
          }
        }
      }
      Column(
        modifier = Modifier
          .weight(1.0F)
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        var checked by rememberSaveable { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxWidth()) {
          Checkbox(
            checked = checked,
            onCheckedChange = {
              checked = it
            }
          )
          Text(text = stringResource(id = R.string.legal__legal_read_confirm_tip))
        }
        Button(
          modifier = Modifier
            .fillMaxWidth(),
          enabled = checked,
          onClick = {
            navigateTo(OnBoardingNavigations.CreatePasscode.destination(forCreate))
          }
        ) {
          Text(text = stringResource(id = R.string.legal__continue_text))
        }
      }
    }
  }
}
