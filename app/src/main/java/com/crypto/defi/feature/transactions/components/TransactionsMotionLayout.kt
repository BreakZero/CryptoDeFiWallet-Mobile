package com.crypto.defi.feature.transactions.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import com.crypto.resource.R

@OptIn(ExperimentalMotionApi::class)
@Composable
fun TransactionsMotionLayout(
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
                Box(
                    modifier = Modifier.height(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(66.dp),
                        painter = painterResource(id = R.drawable.avatar_generic_1),
                        contentDescription = null
                    )
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
