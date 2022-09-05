package com.crypto.core.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AdvanceMenu(
    val title: String,
    val subTitle: String? = null,
    val endValue: String? = null,
    val showIcon: Boolean = true
)

@Composable
fun MenuItemView(
    modifier: Modifier = Modifier, data: AdvanceMenu, action: () -> Unit
) {
    Row(modifier = modifier
        .clickable {
            action.invoke()
        }
        .padding(start = 12.dp, end = 12.dp)
        .defaultMinSize(minHeight = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
        ) {
            Text(text = data.title, color = MaterialTheme.colorScheme.onSecondaryContainer)
            data.subTitle?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        Row() {
            data.endValue?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
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
    modifier: Modifier, header: String, datas: List<AdvanceMenu>, onItemClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = header,
            fontSize = 12.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                datas.forEachIndexed { index, item ->
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(), data = item
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