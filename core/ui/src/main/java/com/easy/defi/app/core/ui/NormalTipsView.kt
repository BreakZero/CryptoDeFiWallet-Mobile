package com.easy.defi.app.core.ui

import androidx.annotation.DrawableRes
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
import com.easy.defi.app.core.designsystem.theme.Spacing

@Composable
fun NormalTipsView(
  @DrawableRes icon: Int,
  title: String,
  message: String,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
      )
      .padding(MaterialTheme.Spacing.medium)
  ) {
    Row(modifier = Modifier.fillMaxWidth()) {
      Text(
        modifier = Modifier
          .weight(1.0f)
          .height(MaterialTheme.Spacing.large),
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = contentColorFor(MaterialTheme.colorScheme.surface)
      )
      Image(
        modifier = Modifier.size(MaterialTheme.Spacing.large),
        painter = painterResource(id = icon),
        contentDescription = null
      )
    }
    Text(
      modifier = Modifier.padding(vertical = MaterialTheme.Spacing.small),
      text = message,
      color = contentColorFor(MaterialTheme.colorScheme.surface)
    )
    Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
      Text(text = "Got it")
    }
  }
}
