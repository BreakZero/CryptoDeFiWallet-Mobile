package com.crypto.defi.feature.assets.transactions.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.crypto.core.extensions.byDecimal2String
import com.crypto.core.ui.Spacing
import com.crypto.defi.models.domain.BaseTransaction
import com.crypto.defi.models.domain.EvmTransaction
import com.crypto.defi.models.domain.TransactionDirection
import com.crypto.resource.R
import timber.log.Timber

@Composable
fun TransactionItemView(
    data: BaseTransaction
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = MaterialTheme.Spacing.space48)
            .clickable {
                Timber.v("${data.timeStamp}, ${data is EvmTransaction}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.Spacing.small,
                    vertical = MaterialTheme.Spacing.extraSmall
                ), verticalAlignment = Alignment.CenterVertically
        ) {
            val isSend = data.direction == TransactionDirection.RECEIVE
            Icon(
                painter = painterResource(
                    id = if (isSend) R.drawable.ic_send else R.drawable.ic_receive
                ),
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = data.hash, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text(text = data.timeStamp, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
            Text(
                text = "${if (isSend) "-" else "+"} ${data.value.byDecimal2String(18, 6)}",
                color = if (isSend) Color.Red else Color.Green
            )
        }
    }
}