@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.crypto.defi.feature.assets.send

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.common.MapKeyConstants
import com.crypto.defi.di.ViewModelFactoryProvider
import com.crypto.defi.navigations.ScannerNavigation
import com.crypto.resource.R
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun sendFormViewModel(
    slug: String
): SendFormViewModel {
    val assistedFactory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).sendFormAssistedViewModelFactory()

    return viewModel(
        factory = object: ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.createSendFormViewModel(slug) as T
            }
        }
    )
}

@Composable
fun SendFormPager(
    savedStateHandle: SavedStateHandle?,
    sendFormViewModel: SendFormViewModel,
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    savedStateHandle?.also { handler ->
        LaunchedEffect(key1 = handler) {
            handler.getStateFlow(MapKeyConstants.KEY_OF_QR_CODE_CONTENT, "").collect {
                Timber.tag("=====").v(it)
                handler.remove<String>(MapKeyConstants.KEY_OF_QR_CODE_CONTENT)
            }
        }
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DeFiAppBar(title = stringResource(id = R.string.send_address__send),
            actions = {
                Icon(
                    modifier = Modifier
                        .padding(end = MaterialTheme.Spacing.medium)
                        .clickable {
                            navigateTo(ScannerNavigation.Scanner)
                        },
                    painter = painterResource(id = R.drawable.ic_scanner),
                    contentDescription = null
                )
            }) {
                navigateUp()
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            ConfirmFormView {

            }
        },
        sheetShape = RoundedCornerShape(
            topEnd = MaterialTheme.Spacing.space24,
            topStart = MaterialTheme.Spacing.space24
        ),
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MaterialTheme.Spacing.medium,
                    vertical = MaterialTheme.Spacing.medium
                )
        ) {
            Column() {
                TextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {})
                Text("")
                TextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {})
                TextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {})

                Text(text = "Miner Fee")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }
            }) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun ConfirmFormView(
    onConfirm: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.Spacing.space48),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(MaterialTheme.Spacing.space48)
                        .padding(MaterialTheme.Spacing.small),
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .weight(1.0f),
                    text = "Payment Details",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(MaterialTheme.Spacing.space48))
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "10.0ETH",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.Spacing.small,
                        horizontal = MaterialTheme.Spacing.medium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.fillMaxWidth(0.3f), text = "Payment Info")
                Text(modifier = Modifier.fillMaxWidth(0.7f), text = "Eth Transfer")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.Spacing.small,
                        horizontal = MaterialTheme.Spacing.medium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.fillMaxWidth(0.3f), text = "To")
                Text(modifier = Modifier.fillMaxWidth(0.7f), text = "Eth Transfer")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.Spacing.small,
                        horizontal = MaterialTheme.Spacing.medium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.fillMaxWidth(0.3f), text = "From")
                Text(modifier = Modifier.fillMaxWidth(0.7f), text = "Eth Transfer")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.Spacing.small,
                        horizontal = MaterialTheme.Spacing.medium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.fillMaxWidth(0.3f), text = "Miner Fee")
                Text(modifier = Modifier.fillMaxWidth(0.7f), text = "Eth Transfer")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.Spacing.medium),
                onClick = {
                    onConfirm()
                }) {
                Text(text = "Confirm")
            }
        }
    }
}