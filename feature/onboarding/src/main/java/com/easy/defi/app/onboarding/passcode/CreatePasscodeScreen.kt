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

package com.easy.defi.app.onboarding.passcode

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.defi.app.core.designsystem.R
import com.easy.defi.app.core.ui.ActionType
import com.easy.defi.app.core.ui.NumberKeyboard
import com.easy.defi.app.core.ui.navigation.NavigationCommand
import com.easy.defi.app.onboarding.OnBoardingNavigations

@Composable
fun CreatePasscodeScreen(
  forCreate: Boolean,
  viewModel: PasscodeViewModel = hiltViewModel(),
  navigateUp: () -> Unit,
  navigateTo: (NavigationCommand) -> Unit,
) {
  LaunchedEffect(key1 = null) {
    viewModel.uiEvent.collect {
      val passcode = it.getOrNull().orEmpty()
      if (it.isSuccess && passcode.isNotEmpty()) {
        // navigateTo()
        if (forCreate) {
        } else {
          navigateTo(OnBoardingNavigations.ImportWallet.destination(passcode))
        }
      }
    }
  }
  DisposableEffect(key1 = null) {
    onDispose {
      viewModel.reset()
    }
  }
  BackHandler() {
    navigateUp()
  }
  Surface(modifier = Modifier.fillMaxSize()) {
    val secondaryColor = MaterialTheme.colorScheme.secondary
    Column(
      modifier = Modifier
        .fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1.0F),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.backgroud_stars),
          contentDescription = null,
          modifier = Modifier.fillMaxWidth()
        )
        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = viewModel.passcodeState.messageLabel,
            modifier = Modifier
              .fillMaxWidth(),
            textAlign = TextAlign.Center
          )
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
          ) {
            val enterSize = viewModel.passcodeState.passcode.length
            if (enterSize == 0) {
              (0 until 6).forEach { _ ->
                Canvas(
                  modifier = Modifier
                    .padding(2.dp)
                    .height(16.dp)
                    .width(16.dp)
                ) {
                  drawCircle(color = secondaryColor, style = Stroke(width = 1.5f))
                }
              }
            } else {
              (0 until enterSize).forEach { _ ->
                Canvas(
                  modifier = Modifier
                    .padding(2.dp)
                    .height(16.dp)
                    .width(16.dp)

                ) {
                  drawCircle(color = secondaryColor)
                }
              }
              (enterSize until 6).forEach { _ ->
                Canvas(
                  modifier = Modifier
                    .padding(2.dp)
                    .height(16.dp)
                    .width(16.dp)
                ) {
                  drawCircle(color = secondaryColor, style = Stroke(width = 1.5f))
                }
              }
            }
          }
          viewModel.passcodeState.error?.let {
            Text(
              text = it,
              color = Color.Red,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
              textAlign = TextAlign.Center
            )
          }
        }
      }
      NumberKeyboard(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(2.dp),
        onNumberClick = {
          when (it.actionType) {
            ActionType.NUMBER -> viewModel.onEvent(PasscodeEvent.Insert(it.number))
            ActionType.BACKSPACE -> viewModel.onEvent(PasscodeEvent.Delete)
            else -> Unit
          }
        }
      )
    }
  }
}
