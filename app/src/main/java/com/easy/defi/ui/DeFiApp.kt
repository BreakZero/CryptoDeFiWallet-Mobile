package com.easy.defi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.easy.defi.app.core.data.util.NetworkMonitor
import com.easy.defi.app.core.designsystem.component.DeFiBackground
import com.easy.defi.app.core.designsystem.component.DeFiGradientBackground
import com.easy.defi.app.core.designsystem.component.DeFiNavigationBar
import com.easy.defi.app.core.designsystem.component.DeFiNavigationBarItem
import com.easy.defi.app.core.designsystem.component.DeFiNavigationRail
import com.easy.defi.app.core.designsystem.component.DeFiNavigationRailItem
import com.easy.defi.app.core.designsystem.icon.Icon
import com.easy.defi.navigation.DeFiNavHost
import com.easy.defi.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun DeFiApp(
  networkMonitor: NetworkMonitor,
  appState: DeFiAppState,
) {
  val background: @Composable (@Composable () -> Unit) -> Unit =
    when (appState.currentTopLevelDestination) {
      TopLevelDestination.WALLET -> { content ->
        DeFiGradientBackground(content = content)
      }
      else -> { content -> DeFiBackground(content = content) }
    }
  background {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
      modifier = Modifier,
      containerColor = Color.Transparent,
      contentWindowInsets = WindowInsets(0, 0, 0, 0),
      snackbarHost = { SnackbarHost(snackbarHostState) },
      bottomBar = {
        if (appState.shouldShowBottomBar) {
          DeFiBottomBar(
            destinations = appState.topLevelDestinations,
            onNavigateToDestination = appState::navigateToTopLevelDestination,
            currentDestination = appState.currentDestination,
          )
        }
      },
    ) { padding ->
      val isOffline by appState.isOffline.collectAsStateWithLifecycle()
      // If user is not connected to the internet show a snack bar to inform them.
      val notConnected = ""
      LaunchedEffect(isOffline) {
        if (isOffline) {
          snackbarHostState.showSnackbar(
            message = notConnected,
            duration = SnackbarDuration.Indefinite,
          )
        }
      }
      Row(
        Modifier
          .fillMaxSize()
          .windowInsetsPadding(
            WindowInsets.safeDrawing.only(
              WindowInsetsSides.Horizontal,
            ),
          ),
      ) {
        if (appState.shouldShowNavRail) {
          DeFiNavRail(
            destinations = appState.topLevelDestinations,
            onNavigateToDestination = appState::navigateToTopLevelDestination,
            currentDestination = appState.currentDestination,
            modifier = Modifier.safeDrawingPadding(),
          )
        }

        DeFiNavHost(
          modifier = Modifier
            .padding(padding)
            .consumedWindowInsets(padding),
          navController = appState.navController,
          onBackClick = appState::onBackClick,
        )
      }
    }
  }
}

@Composable
private fun DeFiNavRail(
  destinations: List<TopLevelDestination>,
  onNavigateToDestination: (TopLevelDestination) -> Unit,
  currentDestination: NavDestination?,
  modifier: Modifier = Modifier,
) {
  DeFiNavigationRail(modifier = modifier) {
    destinations.forEach { destination ->
      val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
      DeFiNavigationRailItem(
        selected = selected,
        onClick = { onNavigateToDestination(destination) },
        icon = {
          val icon = if (selected) {
            destination.selectedIcon
          } else {
            destination.unselectedIcon
          }
          when (icon) {
            is Icon.ImageVectorIcon -> Icon(
              imageVector = icon.imageVector,
              contentDescription = null,
            )
            is Icon.DrawableResourceIcon -> Icon(
              painter = painterResource(id = icon.id),
              contentDescription = null,
            )
          }
        },
        label = { Text(stringResource(destination.iconTextId)) },
      )
    }
  }
}

@Composable
private fun DeFiBottomBar(
  destinations: List<TopLevelDestination>,
  onNavigateToDestination: (TopLevelDestination) -> Unit,
  currentDestination: NavDestination?,
) {
  DeFiNavigationBar {
    destinations.forEach { destination ->
      val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
      DeFiNavigationBarItem(
        selected = selected,
        onClick = { onNavigateToDestination(destination) },
        icon = {
          val icon = if (selected) {
            destination.selectedIcon
          } else {
            destination.unselectedIcon
          }
          when (icon) {
            is Icon.ImageVectorIcon -> Icon(
              imageVector = icon.imageVector,
              contentDescription = null,
            )

            is Icon.DrawableResourceIcon -> Icon(
              painter = painterResource(id = icon.id),
              contentDescription = null,
            )
          }
        },
        label = { Text(stringResource(destination.iconTextId)) },
      )
    }
  }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
  this?.hierarchy?.any {
    it.route?.contains(destination.name, true) ?: false
  } ?: false
