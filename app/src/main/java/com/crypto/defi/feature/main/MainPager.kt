package com.crypto.defi.feature.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import com.crypto.defi.feature.nfts.MainNFTsPager
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
        icon = R.drawable.ic_nav_nft, label = "NFT"
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
            count = menus.size, state = pageState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> {
                    MainAssetsPager(navigateTo = onNavigateTo)
                }
                1 -> {
                    MainNFTsPager()
                }
                2 -> {
                    MainDappsPager()
                }
                3 -> {
                    MainDeFiPager()
                }
                else -> Unit
            }
        }
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.height(56.dp),
            divider = {},
            indicator = {}
        ) {
            menus.forEachIndexed { index, menu ->
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = menu.icon),
                            contentDescription = null
                        )
                        Text(text = menu.label)
                    }
                }
            }
        }
    }
}
