package com.crypto.core.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeFiAppBar(
    navIcon: ImageVector = Icons.Filled.ArrowBack,
    title: String? = null,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {},
    navigateUp: () -> Unit
) {
    SmallTopAppBar(
        colors = colors,
        navigationIcon = {
            IconButton(onClick = {
                navigateUp()
            }) {
                Icon(
                    imageVector = navIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        title = {
            title?.let {
                Text(
                    text = title, color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        actions = actions
    )
}
