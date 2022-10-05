package com.crypto.defi.feature.assets.transactions.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crypto.core.extensions.byDecimal2String
import com.crypto.core.ui.Spacing
import com.crypto.defi.models.domain.Asset
import com.crypto.resource.R
import java.math.BigInteger

@OptIn(ExperimentalMotionApi::class)
@Composable
fun TransactionsMotionLayout(
    asset: Asset?,
    targetValue: Float,
    onSend: () -> Unit,
    onReceive: () -> Unit,
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
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .layoutId("header-content")
        ) {
            Image(
                painter = painterResource(id = R.drawable.backgroud_stars),
                contentDescription = "poster",
                contentScale = ContentScale.FillWidth,
                alpha = 1f - progress
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier.size(MaterialTheme.Spacing.space24),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(asset?.iconUrl)
                            .placeholder(R.drawable.avatar_generic_1)
                            .error(R.drawable.avatar_generic_1)
                            .crossfade(true)
                            .build(), contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = MaterialTheme.Spacing.extraSmall),
                        text = "${asset?.symbol ?: "--"} BALANCE",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye, contentDescription = null,
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
                                (asset?.nativeBalance
                                    ?: BigInteger.ZERO).byDecimal2String(asset?.decimal ?: 0)
                            )
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
                            append(" ${asset?.symbol ?: "--"}")
                        }
                    },
                    modifier = Modifier.padding(MaterialTheme.Spacing.small)
                )
                Text(text = " ~ ${asset?.fiatBalance()?.toPlainString() ?: "--"} USD")
                Row {
                    Column(
                        modifier = Modifier
                            .width(MaterialTheme.Spacing.extraLarge)
                            .clickable {
                                onSend()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                                .padding(MaterialTheme.Spacing.extraSmall)
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
                    Spacer(modifier = Modifier.size(MaterialTheme.Spacing.medium))
                    Column(
                        modifier = Modifier
                            .width(MaterialTheme.Spacing.extraLarge)
                            .clickable {
                                onReceive()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                                .padding(MaterialTheme.Spacing.extraSmall)
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
