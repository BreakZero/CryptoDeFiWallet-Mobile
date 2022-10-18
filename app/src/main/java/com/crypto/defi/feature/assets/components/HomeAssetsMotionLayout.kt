package com.crypto.defi.feature.assets.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import com.crypto.core.ui.Spacing
import com.crypto.resource.R

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeAssetsMotionLayout(
  totalBalance: String,
  targetValue: Float,
  scrollableBody: @Composable () -> Unit
) {
  val progress by animateFloatAsState(
    targetValue = targetValue,
    tween(100)
  )
  MotionLayout(
    start = startConstraintSet(),
    end = endConstraintSet(),
    progress = progress,
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.primary)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .layoutId("header-content"),
      contentAlignment = Alignment.Center
    ) {
      Image(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16 / 9f),
        painter = painterResource(id = R.drawable.backgroud_stars),
        contentDescription = "poster",
        contentScale = ContentScale.FillWidth,
        alpha = 1f - progress
      )
      Column(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(modifier = Modifier) {
          Text(
            text = stringResource(id = R.string.wallet_asset__total_balance_big),
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primaryContainer
          )
          Icon(
            imageVector = Icons.Default.RemoveRedEye, contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically),
            tint = MaterialTheme.colorScheme.primaryContainer
          )
        }
        Text(
          text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
              append("$ ")
            }
            withStyle(
              style = SpanStyle(
                color = MaterialTheme.colorScheme.primaryContainer,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight
              )
            ) {
              append(totalBalance)
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
              append(" USD")
            }
          }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.Spacing.large))
      }
    }
    Box(
      modifier = Modifier.layoutId("content")
    ) {
      scrollableBody()
    }
  }
}

// Constraint Sets defined by using Kotlin DSL option
private fun startConstraintSet() = ConstraintSet {
  val headerContent = createRefFor("header-content")
  val content = createRefFor("content")

  constrain(headerContent) {
    start.linkTo(parent.start)
    top.linkTo(parent.top)
  }

  constrain(content) {
    width = Dimension.fillToConstraints
    top.linkTo(headerContent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
}

private fun endConstraintSet() = ConstraintSet {
  val headerContent = createRefFor("header-content")
  val content = createRefFor("content")

  constrain(headerContent) {
    start.linkTo(parent.start)
    end.linkTo(parent.end)
    bottom.linkTo(parent.top)
  }

  constrain(content) {
    width = Dimension.fillToConstraints
    top.linkTo(headerContent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
}
