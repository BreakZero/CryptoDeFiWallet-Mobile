package com.easy.defi.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeFiAppBar(
  navIcon: ImageVector = Icons.Filled.ArrowBack,
  title: String? = null,
  actions: @Composable RowScope.() -> Unit = {},
  navigateUp: () -> Unit,
) {
  TopAppBar(
    modifier = Modifier
      .background(
        Brush.verticalGradient(
          listOf(MaterialTheme.colorScheme.primary, Color.White)
        )
      ),
    colors = TopAppBarDefaults.smallTopAppBarColors(
      containerColor = Color.Transparent
    ),
    navigationIcon = {
      IconButton(
        onClick = {
          navigateUp()
        }
      ) {
        Icon(
          imageVector = navIcon,
          contentDescription = "",
          tint = MaterialTheme.colorScheme.primaryContainer
        )
      }
    },
    title = {
      title?.let {
        Text(
          text = title,
          color = MaterialTheme.colorScheme.primaryContainer,
          style = MaterialTheme.typography.titleMedium
        )
      }
    },
    actions = actions
  )
}
