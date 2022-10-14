package com.crypto.onboarding.presentation.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.AdvanceMenu
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.MenuItemView
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.core.ui.utils.SetStatusColor
import com.crypto.onboarding.presentation.OnboardingNavigations
import com.crypto.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreen(
  forCreate: Boolean,
  navigateUp: () -> Unit,
  navigateTo: (NavigationCommand) -> Unit
) {
  SetStatusColor(statusColor = MaterialTheme.colorScheme.surface)
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      DeFiAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = Color.Transparent
        )
      ) {
        navigateUp()
      }
    }
  ) {
    Column(
      modifier = Modifier.padding(it)
    ) {
      Text(
        modifier = Modifier.padding(start = MaterialTheme.Spacing.medium),
        text = stringResource(id = R.string.legal__legal),
        style = MaterialTheme.typography.titleLarge
      )
      Text(
        modifier = Modifier.padding(horizontal = MaterialTheme.Spacing.medium),
        text = stringResource(
          id = R.string.legal__legal_tips
        )
      )
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.Spacing.medium),
        shape = RoundedCornerShape(8.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          MenuItemView(
            modifier = Modifier.fillMaxWidth(),
            data = AdvanceMenu(title = stringResource(id = R.string.legal__terms_of_service))
          ) {

          }
          Divider()
          MenuItemView(
            modifier = Modifier.fillMaxWidth(),
            data = AdvanceMenu(title = stringResource(id = R.string.legal__privacy_notice))
          ) {

          }
        }
      }
      Column(
        modifier = Modifier
          .weight(1.0F)
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.Spacing.medium),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        var checked by rememberSaveable { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxWidth()) {
          Checkbox(checked = checked, onCheckedChange = {
            checked = it
          })
          Text(text = stringResource(id = R.string.legal__legal_read_confirm_tip))
        }
        Button(
          modifier = Modifier
            .fillMaxWidth(),
          enabled = checked,
          onClick = {
            navigateTo(OnboardingNavigations.CreatePasscode.destination(forCreate))
          }) {
          Text(text = stringResource(id = R.string.legal__continue_text))
        }
      }
    }
  }
}
