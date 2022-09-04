package com.crypto.onboarding.presentation.legal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.MenuData
import com.crypto.core.ui.composables.MenuItemView
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.onboarding.presentation.OnboardingNavigations
import com.crypto.resource.R

@Composable
fun LegalPager(
    forCreate: Boolean,
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            DeFiAppBar() {
                navigateUp()
            }
            Text(
                modifier = Modifier.padding(start = MaterialTheme.Spacing.medium),
                text = stringResource(id = R.string.legal__legal),
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight
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
                        data = MenuData(title = stringResource(id = R.string.legal__terms_of_service))
                    ) {

                    }
                    Divider()
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(),
                        data = MenuData(title = stringResource(id = R.string.legal__privacy_notice))
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
                val checkedState = remember {
                    mutableStateOf(false)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(checked = checkedState.value, onCheckedChange = {
                        checkedState.value = it
                    })
                    Text(text = stringResource(id = R.string.legal__legal_read_confirm_tip))
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = checkedState.value,
                    onClick = {
                        navigateTo(OnboardingNavigations.CreatePasscode.destination(forCreate))
                    }) {
                    Text(text = stringResource(id = R.string.legal__continue_text))
                }
            }
        }
    }
}