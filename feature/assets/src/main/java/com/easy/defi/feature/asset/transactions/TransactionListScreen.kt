package com.easy.defi.feature.asset.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.component.brushBackground
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.data.BaseTransaction
import com.easy.defi.app.core.model.data.EvmTransaction
import com.easy.defi.app.core.model.data.TransactionDirection
import com.easy.defi.app.core.model.x.byDecimal2String
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.core.ui.LoadingIndicator
import timber.log.Timber
import java.math.BigInteger

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TransactionListScreen(
  listViewModel: TransactionListViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {
  val uiState by listViewModel.transactionListUiState.collectAsState()
  val transactionPaging = uiState.transactionPaging.collectAsLazyPagingItems()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .brushBackground(
        listOf(
          MaterialTheme.colorScheme.primary,
          MaterialTheme.colorScheme.surface,
          MaterialTheme.colorScheme.surface
        )
      )
  ) {
    DeFiAppBar {
      onBackClick()
    }
    LazyColumn(
      modifier = Modifier
        .fillMaxSize(),
      contentPadding = PaddingValues(
        vertical = MaterialTheme.spacing.medium,
        horizontal = MaterialTheme.spacing.small
      ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
      item {
        TransactionListHeader(
          asset = uiState.asset,
          onSend = {},
          onReceive = {}
        )
      }
      items(items = transactionPaging, key = { it.hash }) { transaction ->
        transaction?.let {
          TransactionItemView(modifier = Modifier, data = it) {
            Timber.v("${it.timeStamp}, ${it is EvmTransaction}")
          }
        }
      }
      when (transactionPaging.loadState.refresh) {
        is LoadState.Loading -> {
          item {
            Box(
              modifier = Modifier
                .fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              LoadingIndicator(animating = true)
            }
          }
        }
        is LoadState.Error -> {
          item {
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "somethings went wrong")
            }
          }
        }
        else -> Unit
      }
      if (transactionPaging.loadState.append.endOfPaginationReached) {
        item {
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "-- Ending --")
          }
        }
      }
      if (transactionPaging.loadState.append is LoadState.Loading) {
        item {
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            LoadingIndicator(animating = true)
          }
        }
      }
      item {
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
      }
    }
  }
}

@Composable
private fun TransactionListHeader(
  asset: Asset?,
  onSend: () -> Unit,
  onReceive: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(16 / 9f)
      .padding(top = 32.dp, bottom = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        modifier = androidx.compose.ui.Modifier.size(MaterialTheme.spacing.space24),
        model = ImageRequest.Builder(LocalContext.current)
          .data(asset?.iconUrl)
          .placeholder(R.drawable.avatar_generic_1)
          .error(R.drawable.avatar_generic_1)
          .crossfade(true)
          .build(),
        contentDescription = null
      )
      Text(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        text = "${asset?.symbol ?: "--"} BALANCE",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primaryContainer
      )
      Icon(
        imageVector = Icons.Default.RemoveRedEye,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primaryContainer
      )
    }
    Text(
      text = buildAnnotatedString {
        withStyle(
          style = SpanStyle(
            color = MaterialTheme.colorScheme.primaryContainer,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight
          )
        ) {
          append(
            (
              asset?.nativeBalance
                ?: BigInteger.ZERO
              ).byDecimal2String(asset?.decimal ?: 0)
          )
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
          append(" ${asset?.symbol ?: "--"}")
        }
      },
      modifier = androidx.compose.ui.Modifier.padding(MaterialTheme.spacing.small)
    )
    Text(text = " ~ ${asset?.fiatBalance()?.toPlainString() ?: "--"} USD")
    Row {
      Column(
        modifier = androidx.compose.ui.Modifier
          .width(MaterialTheme.spacing.extraLarge)
          .clickable {
            onSend()
          },
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(MaterialTheme.spacing.extraSmall)
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_send),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primaryContainer)
          )
        }
        Text(
          text = stringResource(id = R.string.transaction_list__send),
          color = MaterialTheme.colorScheme.primaryContainer
        )
      }
      Spacer(modifier = androidx.compose.ui.Modifier.size(MaterialTheme.spacing.medium))
      Column(
        modifier = androidx.compose.ui.Modifier
          .width(MaterialTheme.spacing.extraLarge)
          .clickable {
            onReceive()
          },
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(MaterialTheme.spacing.extraSmall)
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_receive),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primaryContainer)
          )
        }
        Text(
          text = stringResource(id = R.string.transaction_list__receive),
          color = MaterialTheme.colorScheme.primaryContainer
        )
      }
    }
  }
}

@Composable
fun TransactionItemView(
  modifier: Modifier,
  data: BaseTransaction,
  onItemClick: () -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(MaterialTheme.spacing.extraLarge),
    elevation = CardDefaults.cardElevation(
      defaultElevation = MaterialTheme.spacing.extraSmall
    ),
    shape = RoundedCornerShape(MaterialTheme.spacing.space12)
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .clickable {
          onItemClick.invoke()
        }
        .padding(
          horizontal = MaterialTheme.spacing.small,
          vertical = MaterialTheme.spacing.extraSmall
        ),
      verticalAlignment = Alignment.CenterVertically
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
