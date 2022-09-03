package com.crypto.onboarding.presentation.legal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.crypto.core.common.Navigator
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.resource.R

@Composable
fun LegalPager(
    navigateUp: () -> Unit,
    navigateTo: (Navigator) -> Unit
) {
    Column {
        DeFiAppBar() {
            navigateUp()
        }
        Text(text = stringResource(id = R.string.legal__legal_tips))
        Card() {

        }
        Row() {
            Checkbox(checked = false, onCheckedChange = {

            })
            Text(text = stringResource(id = R.string.legal__legal_read_confirm_tip))
        }
    }
}