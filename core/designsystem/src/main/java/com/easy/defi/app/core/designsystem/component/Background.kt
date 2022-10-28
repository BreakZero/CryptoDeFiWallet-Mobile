/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.defi.app.core.designsystem.component

import android.content.res.Configuration
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.easy.defi.app.core.designsystem.theme.DeFiWalletTheme
import com.easy.defi.app.core.designsystem.theme.LocalBackgroundTheme
import com.easy.defi.app.core.designsystem.theme.LocalGradientColors
import kotlin.math.tan
import org.intellij.lang.annotations.Language

/**
 * The main background for the app.
 * Uses [LocalBackgroundTheme] to set the color and tonal elevation of a [Box].
 *
 * @param modifier Modifier to be applied to the background.
 * @param content The background content.
 */
@Composable
fun DeFiBackground(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  val color = LocalBackgroundTheme.current.color
  val tonalElevation = LocalBackgroundTheme.current.tonalElevation
  Surface(
    color = if (color == Color.Unspecified) Color.Transparent else color,
    tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
    modifier = modifier.fillMaxSize()
  ) {
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
      content()
    }
  }
}

/**
 * A gradient background for select screens. Uses [LocalBackgroundTheme] to set the gradient colors
 * of a [Box].
 *
 * @param modifier Modifier to be applied to the background.
 * @param topColor The top gradient color to be rendered.
 * @param bottomColor The bottom gradient color to be rendered.
 * @param content The background content.
 */
@Composable
fun DeFiGradientBackground(
  modifier: Modifier = Modifier,
  topColor: Color = LocalGradientColors.current.primary,
  bottomColor: Color = LocalGradientColors.current.secondary,
  content: @Composable () -> Unit,
) {
  val currentTopColor by rememberUpdatedState(topColor)
  val currentBottomColor by rememberUpdatedState(bottomColor)
  DeFiBackground(modifier) {
    Box(
      Modifier
        .fillMaxSize()
        .drawWithCache {
          // Compute the start and end coordinates such that the gradients are angled 11.06
          // degrees off the vertical axis
          val offset = size.height * tan(
            Math
              .toRadians(11.06)
              .toFloat()
          )

          val start = Offset(size.width / 2 + offset / 2, 0f)
          val end = Offset(size.width / 2 - offset / 2, size.height)

          // Create the top gradient that fades out after the halfway point vertically
          val topGradient = Brush.linearGradient(
            0f to if (currentTopColor == Color.Unspecified) {
              Color.Transparent
            } else {
              currentTopColor
            },
            0.724f to Color.Transparent,
            start = start,
            end = end
          )
          // Create the bottom gradient that fades in before the halfway point vertically
          val bottomGradient = Brush.linearGradient(
            0.2552f to Color.Transparent,
            1f to if (currentBottomColor == Color.Unspecified) {
              Color.Transparent
            } else {
              currentBottomColor
            },
            start = start,
            end = end
          )

          onDrawBehind {
            // There is overlap here, so order is important
            drawRect(topGradient)
            drawRect(bottomGradient)
          }
        }
    ) {
      content()
    }
  }
}

/**
 * Multipreview annotation that represents light and dark themes. Add this annotation to a
 * composable to render the both themes.
 */
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews

@ThemePreviews
@Composable
fun BackgroundDefault() {
  DeFiWalletTheme(disableDynamicTheming = true) {
    DeFiBackground(Modifier.size(100.dp), content = {})
  }
}

@ThemePreviews
@Composable
fun BackgroundDynamic() {
  DeFiWalletTheme {
    DeFiBackground(Modifier.size(100.dp), content = {})
  }
}

@ThemePreviews
@Composable
fun BackgroundAndroid() {
  DeFiWalletTheme(androidTheme = true) {
    DeFiBackground(Modifier.size(100.dp), content = {})
  }
}

@ThemePreviews
@Composable
fun GradientBackgroundDefault() {
  DeFiWalletTheme(disableDynamicTheming = true) {
    DeFiGradientBackground(Modifier.size(100.dp), content = {})
  }
}

@ThemePreviews
@Composable
fun GradientBackgroundDynamic() {
  DeFiWalletTheme {
    DeFiGradientBackground(Modifier.size(100.dp), content = {})
  }
}

@ThemePreviews
@Composable
fun GradientBackgroundAndroid() {
  DeFiWalletTheme(androidTheme = true) {
    DeFiGradientBackground(Modifier.size(100.dp), content = {})
  }
}

fun Modifier.yellowBackground(
  color: Color,
): Modifier = this.composed {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    // produce updating time in seconds variable to pass into shader
    val time by produceState(0f) {
      while (true) {
        withInfiniteAnimationFrameMillis {
          value = it / 1000f
        }
      }
    }
    Modifier.drawWithCache {
      val shader = RuntimeShader(SHADER)
      val shaderBrush = ShaderBrush(shader)
      shader.setFloatUniform("iResolution", size.width, size.height)
      shader.setFloatUniform("iTime", time)
      // Pass the color to support color space automatically
      shader.setColorUniform(
        "iColor",
        android.graphics.Color.valueOf(color.red, color.green, color.blue, color.alpha)
      )
      onDrawBehind {
        drawRect(shaderBrush)
      }
    }
  } else {
    Modifier.drawWithCache {
      val gradientBrush = Brush.verticalGradient(listOf(color, White))
      onDrawBehind {
        drawRect(gradientBrush)
      }
    }
  }
}

@Language("AGSL")
val SHADER = """
    uniform float2 iResolution;
    uniform float iTime;
    layout(color) uniform half4 iColor;

    float calculateColorMultiplier(float yCoord, float factor) {
        return step(yCoord, 1.0 + factor * 2.0) - step(yCoord, factor - 0.1);
    }
    float4 main(in float2 fragCoord) {
        // Config values
        const float speedMultiplier = 1.5;
        const float waveDensity = 1.0;
        const float loops = 8.0;
        const float energy = 0.6;

        // Calculated values
        float2 uv = fragCoord / iResolution.xy;
        float3 color = iColor.rgb;
        float timeOffset = iTime * speedMultiplier;
        float hAdjustment = uv.x * 4.3;
        float3 loopColor = vec3(1.0 - color.r, 1.0 - color.g, 1.0 - color.b) / loops;

        for (float i = 1.0; i <= loops; i += 1.0) {
            float loopFactor = i * 0.1;
            float sinInput = (timeOffset + hAdjustment) * energy;
            float curve = sin(sinInput) * (1.0 - loopFactor) * 0.05;
            float colorMultiplier = calculateColorMultiplier(uv.y, loopFactor);
            color += loopColor * colorMultiplier;

            // Offset for next loop
            uv.y += curve;
        }

        return float4(color, 1.0);
    }
""".trimIndent()
