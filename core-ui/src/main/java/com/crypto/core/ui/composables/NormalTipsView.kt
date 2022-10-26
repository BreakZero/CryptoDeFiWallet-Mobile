package com.crypto.core.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.models.NormalTips

@Composable
fun NormalTipsView(
  tips: NormalTips,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
      )
      .padding(MaterialTheme.Spacing.medium),
  ) {
    Row(modifier = Modifier.fillMaxWidth()) {
      Text(
        modifier = Modifier
          .weight(1.0f)
          .height(MaterialTheme.Spacing.large),
        text = tips.title,
        style = MaterialTheme.typography.titleMedium,
        color = contentColorFor(MaterialTheme.colorScheme.surface),
      )
      Image(
        modifier = Modifier.size(MaterialTheme.Spacing.large),
        painter = painterResource(id = tips.iconRes),
        contentDescription = null,
      )
    }
    Text(
      modifier = Modifier.padding(vertical = MaterialTheme.Spacing.small),
      text = tips.message,
      color = contentColorFor(MaterialTheme.colorScheme.surface),
    )
    Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
      Text(text = "Got it")
    }
  }
}
