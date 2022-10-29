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

package com.easy.defi.app.onboarding.wallet.imports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.core.ui.LoadingButton
import com.easy.defi.app.core.ui.UiEvent
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImportWordsScreen(
  passcode: String,
  viewModel: WalletImportViewModel = hiltViewModel(),
  navigateUp: () -> Unit
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val context = LocalContext.current
  val importState = viewModel.state
  LaunchedEffect(key1 = keyboardController) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.Success -> {
        }
        is UiEvent.ShowSnackbar -> {
          Timber.v(event.message.asString(context))
        }
        is UiEvent.NavigateUp -> {
          navigateUp()
        }
        else -> Unit
      }
    }
  }
  Scaffold(
    modifier = Modifier,
    topBar = {
      DeFiAppBar(
        title = stringResource(id = R.string.import_wallet__import_wallet),
        actions = {
          Icon(imageVector = Icons.Default.QrCode, contentDescription = null)
        }
      ) {
        viewModel.onNavigateUp()
      }
    }
  ) {
    Column(
      modifier = Modifier.padding(it)
    ) {
      TextField(
        value = importState.phrase,
        onValueChange = {
          viewModel.onEvent(ImportEvent.OnPhraseChange(it))
        },
        textStyle = TextStyle(color = Color.Black),
        keyboardActions = KeyboardActions(
          onDone = {
            keyboardController?.hide()
          }
        ),
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Done
        ),
        modifier = Modifier
          .padding(MaterialTheme.spacing.medium)
          .defaultMinSize(minHeight = 128.dp)
          .fillMaxWidth()
          .onFocusChanged {
            viewModel.onEvent(ImportEvent.OnFocusChange(it.isFocused))
          }
      )
      LoadingButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.spacing.medium),
        loading = viewModel.state.inProgress,
        onClick = {
          viewModel.onEvent(ImportEvent.OnImportClick(passcode))
          keyboardController?.hide()
        }
      ) {
        Text(text = stringResource(id = R.string.import_wallet__restore))
      }
    }
  }
}
