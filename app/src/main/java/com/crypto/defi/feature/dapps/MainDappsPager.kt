package com.crypto.defi.feature.dapps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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
fun MainDappsPager() {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        //
                    }) {
                        Image(
                            modifier = Modifier.size(MaterialTheme.Spacing.space48),
                            painter = painterResource(id = R.drawable.avatar_generic_1),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Icon(
                        modifier = Modifier.padding(end = MaterialTheme.Spacing.medium),
                        painter = painterResource(id = R.drawable.ic_scanner),
                        contentDescription = null
                    )
                },
                title = {
                    Column {
                        Text(
                            text = "Wallet Name",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = stringResource(id = R.string.avatar_wallet_layout__view_settings),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            )
        }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "main dapps")
        }
    }
}