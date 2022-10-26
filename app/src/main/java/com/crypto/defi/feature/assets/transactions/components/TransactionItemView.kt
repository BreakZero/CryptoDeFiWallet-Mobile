package com.crypto.defi.feature.assets.transactions.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.crypto.core.extensions.byDecimal2String
import com.crypto.core.ui.Spacing
import com.crypto.defi.models.domain.BaseTransaction
import com.crypto.defi.models.domain.TransactionDirection
import com.crypto.resource.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItemView(
  modifier: Modifier,
  data: BaseTransaction,
  onItemClick: () -> Unit,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(MaterialTheme.Spacing.extraLarge),
    elevation = CardDefaults.cardElevation(
      defaultElevation = MaterialTheme.Spacing.extraSmall,
    ),
    shape = RoundedCornerShape(MaterialTheme.Spacing.space12),
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .clickable {
          onItemClick.invoke()
        }
        .padding(
          horizontal = MaterialTheme.Spacing.small,
          vertical = MaterialTheme.Spacing.extraSmall,
        ),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val isSend = data.direction == TransactionDirection.RECEIVE
      Icon(
        painter = painterResource(
          id = if (isSend) R.drawable.ic_send else R.drawable.ic_receive,
        ),
        contentDescription = null,
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(text = data.hash, overflow = TextOverflow.Ellipsis, maxLines = 1)
        Text(text = data.timeStamp, overflow = TextOverflow.Ellipsis, maxLines = 1)
      }
      Text(
        text = "${if (isSend) "-" else "+"} ${data.value.byDecimal2String(18, 6)}",
        color = if (isSend) Color.Red else Color.Green,
      )
    }
  }
}
