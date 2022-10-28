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
import androidx.compose.material3.TopAppBarDefaults
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
import com.easy.defi.app.core.designsystem.theme.Spacing
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.core.ui.LoadingButton
import com.easy.defi.app.core.ui.UiEvent
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImportWordsScreen(
  passcode: String,
  viewModel: WalletImportViewModel = hiltViewModel(),
  navigateUp: () -> Unit,
  navigateMain: () -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val context = LocalContext.current
  val importState = viewModel.state
  LaunchedEffect(key1 = keyboardController) {
    viewModel.uiEvent.collect { event ->
      when (event) {
        is UiEvent.Success -> {
          navigateMain()
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
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = Color.Transparent,
        ),
        actions = {
          Icon(imageVector = Icons.Default.QrCode, contentDescription = null)
        },
      ) {
        viewModel.onNavigateUp()
      }
    },
  ) {
    Column(
      modifier = Modifier.padding(it),
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
          },
        ),
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Done,
        ),
        modifier = Modifier
          .padding(MaterialTheme.Spacing.medium)
          .defaultMinSize(minHeight = 128.dp)
          .fillMaxWidth()
          .onFocusChanged {
            viewModel.onEvent(ImportEvent.OnFocusChange(it.isFocused))
          },
      )
      LoadingButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.Spacing.medium),
        loading = viewModel.state.inProgress,
        onClick = {
          viewModel.onEvent(ImportEvent.OnImportClick(passcode))
          keyboardController?.hide()
        },
      ) {
        Text(text = stringResource(id = R.string.import_wallet__restore))
      }
    }
  }
}
