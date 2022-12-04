package com.easy.defi.app.settings.network

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.defi.app.core.ui.DeFiAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCurrencyScreen(
  currencyViewModel: SetCurrencyViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      DeFiAppBar(
        actions = {
          IconButton(onClick = {
            onBackClick()
          }) {
            Icon(
              imageVector = Icons.Default.Done,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primaryContainer
            )
          }
        }
      ) {
        onBackClick()
      }
    }
  ) {
    val currencyUiState by currencyViewModel.currencyState.collectAsState()
    LazyColumn(
      modifier = Modifier.padding(it)
    ) {
      items(
        items = currencyUiState.supportList,
        key = {
          it.code
        }
      ) { currency ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = currency.code == currencyUiState.selected.code,
            onClick = {
              currencyViewModel.updateCurrency(currency)
            }
          )
          Text(text = "${currency.code} (${currency.symbol})")
        }
      }
    }
  }
}
