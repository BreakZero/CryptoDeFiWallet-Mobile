package com.easy.defi.feature.asset.send

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.designsystem.theme.spacing
import com.easy.defi.app.core.model.data.Asset
import com.easy.defi.app.core.model.data.TransactionPlan
import com.easy.defi.app.core.model.x.byDecimal2String
import com.easy.defi.app.core.ui.DeFiAppBar
import com.easy.defi.app.core.ui.LoadingButton
import timber.log.Timber

@OptIn(
  ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
  ExperimentalLifecycleComposeApi::class
)
@Composable
internal fun SendFormScreen(
  sendFormViewModel: SendFormViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val uiState by sendFormViewModel.sendFormState.collectAsStateWithLifecycle()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .imePadding()
  ) {
    DeFiAppBar() {
      onBackClick()
    }
    Column(
      modifier = Modifier
        .padding(MaterialTheme.spacing.medium)
        .weight(1f)
    ) {
      Text(text = stringResource(id = R.string.send__to))
      Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
      BasicTextField(
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(
            minWidth = TextFieldDefaults.MinWidth,
            minHeight = TextFieldDefaults.MinHeight
          )
          .clip(RoundedCornerShape(MaterialTheme.spacing.small))
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(
            vertical = MaterialTheme.spacing.medium,
            horizontal = MaterialTheme.spacing.medium
          ),
        textStyle = LocalTextStyle.current,
        maxLines = 2,
        keyboardOptions = KeyboardOptions.Default.copy(
          keyboardType = KeyboardType.Text
        ),
        value = uiState.to,
        onValueChange = {
          sendFormViewModel.onToChanged(it)
        }
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = MaterialTheme.spacing.extraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = stringResource(id = R.string.send__enter_amount))
        Text(text = "")
      }
      BasicTextField(
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(
            minWidth = TextFieldDefaults.MinWidth,
            minHeight = TextFieldDefaults.MinHeight
          )
          .clip(
            RoundedCornerShape(
              topStart = MaterialTheme.spacing.small,
              topEnd = MaterialTheme.spacing.small
            )
          )
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(
            vertical = MaterialTheme.spacing.medium,
            horizontal = MaterialTheme.spacing.medium
          ),
        textStyle = LocalTextStyle.current,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
          keyboardType = KeyboardType.Number
        ),
        value = uiState.amount.orEmpty(),
        onValueChange = {
          sendFormViewModel.onAmountChanged(it)
        }
      )
      Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
      BasicTextField(
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(
            minWidth = TextFieldDefaults.MinWidth,
            minHeight = TextFieldDefaults.MinHeight
          )
          .clip(
            RoundedCornerShape(
              bottomStart = MaterialTheme.spacing.small,
              bottomEnd = MaterialTheme.spacing.small
            )
          )
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(
            vertical = MaterialTheme.spacing.medium,
            horizontal = MaterialTheme.spacing.medium
          ),
        textStyle = LocalTextStyle.current,
        maxLines = 5,
        keyboardOptions = KeyboardOptions.Default.copy(
          keyboardType = KeyboardType.Text
        ),
        value = uiState.memo,
        onValueChange = {
          sendFormViewModel.onMemoChanged(it)
        }
      )
      Text(text = "Miner Fee")
    }
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.spacing.medium),
      onClick = {
        keyboardController?.hide()
        sendFormViewModel.sign()
      }
    ) {
      Text(text = "Next")
    }
    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
  }
  if (uiState.plan != TransactionPlan.EmptyPlan) {
    ConfirmDialog(asset = uiState.asset!!, plan = uiState.plan) {
      Timber.tag("=====").v("confirm")
    }
  }
}

@Composable
private fun ConfirmDialog(
  asset: Asset,
  plan: TransactionPlan,
  onConfirm: () -> Unit
) {
  Dialog(onDismissRequest = {}) {
    var loading by remember {
      mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(MaterialTheme.spacing.space48),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
          },
          modifier = Modifier
            .size(MaterialTheme.spacing.space48)
            .padding(MaterialTheme.spacing.small)
        ) {
          Icon(imageVector = Icons.Default.Close, contentDescription = null)
        }
        Text(
          modifier = Modifier
            .weight(1.0f),
          text = "Payment Details",
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.space48))
      }
      Divider()
      Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "${plan.amount.byDecimal2String(asset.decimal)} ${asset.symbol}",
        style = MaterialTheme.typography.titleLarge
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            vertical = MaterialTheme.spacing.small,
            horizontal = MaterialTheme.spacing.medium
          ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(modifier = Modifier.fillMaxWidth(0.3f), text = "Payment Info")
        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.small)
            .weight(1.0F),
          text = plan.action
        )
      }
      Divider(thickness = 0.2.dp)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            vertical = MaterialTheme.spacing.small,
            horizontal = MaterialTheme.spacing.medium
          ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(modifier = Modifier.fillMaxWidth(0.3f), text = "To")
        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.small)
            .weight(1.0F),
          text = plan.to
        )
      }
      Divider(thickness = 0.2.dp)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            vertical = MaterialTheme.spacing.small,
            horizontal = MaterialTheme.spacing.medium
          ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(modifier = Modifier.fillMaxWidth(0.3f), text = "From")
        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.small)
            .weight(1.0F),
          text = plan.from
        )
      }
      Divider(thickness = 0.2.dp)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            vertical = MaterialTheme.spacing.small,
            horizontal = MaterialTheme.spacing.medium
          ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(modifier = Modifier.fillMaxWidth(0.3f), text = "Miner Fee")
        Text(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.small)
            .weight(1.0F),
          text = "${plan.fee.byDecimal2String(asset.feeDecimal())} ${asset.feeSymbol()}"
        )
      }
      LoadingButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.spacing.medium),
        loading = loading,
        onClick = {
          if (!loading) {
            loading = true
            onConfirm()
          }
        }
      ) {
        Text(text = stringResource(id = R.string.send__confirm))
      }
    }
  }
}
