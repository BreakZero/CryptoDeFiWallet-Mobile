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
  navigateUp: () -> Unit
) {
  TopAppBar(
    modifier = Modifier
      .background(
        Brush.verticalGradient(
          listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
          )
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
