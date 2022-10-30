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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.easy.defi.app.core.designsystem.theme.spacing

data class AdvanceMenu(
  val title: String,
  val subTitle: String? = null,
  val endValue: String? = null,
  val showIcon: Boolean = true
)

@Composable
fun MenuItemView(
  modifier: Modifier = Modifier,
  data: AdvanceMenu,
  action: () -> Unit
) {
  Row(
    modifier = modifier
      .background(MaterialTheme.colorScheme.background)
      .clickable {
        action.invoke()
      }
      .padding(horizontal = MaterialTheme.spacing.space12)
      .height(MaterialTheme.spacing.space56),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier
    ) {
      Text(text = data.title, color = MaterialTheme.colorScheme.onBackground)
      data.subTitle?.let {
        Text(
          text = it,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSecondaryContainer
        )
      }
    }
    Row(
      modifier = Modifier.fillMaxHeight(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      data.endValue?.let {
        Text(
          text = it,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSecondaryContainer
        )
      }
      if (data.showIcon) {
        Icon(
          imageVector = Icons.Filled.ArrowRight,
          tint = MaterialTheme.colorScheme.onSecondaryContainer,
          contentDescription = null
        )
      }
    }
  }
}

@Composable
fun MenuBlockView(
  modifier: Modifier,
  header: String,
  datas: List<AdvanceMenu>,
  onItemClick: (Int) -> Unit
) {
  Column(
    modifier = modifier
  ) {
    Text(
      text = header,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier.padding(
        top = MaterialTheme.spacing.medium,
        bottom = MaterialTheme.spacing.small
      )
    )
    Card(
      modifier = Modifier
        .fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
      ),
      elevation = CardDefaults.cardElevation(
        defaultElevation = MaterialTheme.spacing.extraSmall
      ),
      shape = RoundedCornerShape(MaterialTheme.spacing.small)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        datas.forEachIndexed { index, item ->
          MenuItemView(
            modifier = Modifier.fillMaxWidth(),
            data = item
          ) {
            onItemClick.invoke(index)
          }
          if (index < datas.size - 1) {
            Divider(
              modifier = Modifier
                .fillMaxWidth()
                .height(0.2.dp)
            )
          }
        }
      }
    }
  }
}
