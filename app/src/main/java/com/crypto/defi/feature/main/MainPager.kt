package com.crypto.defi.feature.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.feature.assets.MainAssetsPager
import com.crypto.defi.feature.dapps.MainDappsPager
import com.crypto.defi.feature.defi.MainDeFiPager
import com.crypto.resource.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

data class NavMenu(
    @DrawableRes val icon: Int,
    val label: String
)

val navMenus = listOf(
    NavMenu(
        icon = R.drawable.ic_nav_wallet, label = "Wallet"
    ),
    NavMenu(
        icon = R.drawable.ic_nav_dapp, label = "Dapps"
    ),
    NavMenu(
        icon = R.drawable.ic_nav_defi, label = "Earn"
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainPager(
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateTo: (NavigationCommand) -> Unit
) {
    val pageState = rememberPagerState()
    val tabIndex = pageState.currentPage
    val scope = rememberCoroutineScope()
    val menus by remember {
        mutableStateOf(navMenus)
    }

    val systemUIController = rememberSystemUiController()
    val useDartIcons = isSystemInDarkTheme()
    val statusColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(key1 = pageState.currentPage) {
        systemUIController.setStatusBarColor(
            statusColor, useDartIcons
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            count = menus.size, state = pageState
        ) { page ->
            when (page) {
                0 -> {
                    MainAssetsPager()
                }
                1 -> {
                    MainDappsPager()
                }
                2 -> {
                    MainDeFiPager()
                }
                else -> Unit
            }
        }
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.height(56.dp),
            indicator = {}
        ) {
            menus.forEachIndexed { index, bottomMenuItem ->
                Tab(
                    selected = index == tabIndex,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = bottomMenuItem.icon),
                            contentDescription = null
                        )
                        Text(text = bottomMenuItem.label)
                    }
                }
            }
        }
    }
}
