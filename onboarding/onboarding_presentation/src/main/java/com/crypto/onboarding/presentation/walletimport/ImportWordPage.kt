package com.crypto.onboarding.presentation.walletimport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.common.UiEvent
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.resource.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImportWordsPager(
    viewModel: WalletImportViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val importState = viewModel.state
    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {

                }
                is UiEvent.ShowSnackbar -> {

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
                navigateUp.invoke()
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
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier
                    .padding(MaterialTheme.Spacing.medium)
                    .defaultMinSize(minHeight = 128.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.onEvent(ImportEvent.OnFocusChange(it.isFocused))
                    }
            )
            Button(
                modifier = Modifier
                    .height(MaterialTheme.Spacing.extraLarge)
                    .fillMaxWidth()
                    .padding(MaterialTheme.Spacing.medium),
                shape = RoundedCornerShape(MaterialTheme.Spacing.space24),
                onClick = {
                    viewModel.onEvent(ImportEvent.OnImportClick)
                    keyboardController?.hide()
                }
            ) {
                Text(text = stringResource(id = R.string.import_wallet__restore))
            }
        }
    }
}