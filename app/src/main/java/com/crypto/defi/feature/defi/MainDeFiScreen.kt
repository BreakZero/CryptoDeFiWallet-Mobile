package com.crypto.defi.feature.defi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.crypto.core.ui.Spacing
import com.crypto.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDeFiScreen() {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                    }) {
                        Image(
                            modifier = Modifier.size(MaterialTheme.Spacing.space48),
                            painter = painterResource(id = R.drawable.avatar_generic_1),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "Wallet Name",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = stringResource(id = R.string.avatar_wallet_layout__view_settings),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            )
        }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "DeFi dapps")
        }
    }
}