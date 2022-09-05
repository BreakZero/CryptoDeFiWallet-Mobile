///*
//package com.crypto.defi.feature.main
//
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.crypto.core.ui.routers.NavigationCommand
//import com.google.accompanist.pager.ExperimentalPagerApi
//import com.google.accompanist.pager.HorizontalPager
//import com.google.accompanist.pager.rememberPagerState
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import kotlinx.coroutines.launch
//
//val menus = listOf(
//    BottomMenuItem(
//        icon = R.drawable.ic_bottom_menu_wallet, label = "Wallet"
//    ),
//    BottomMenuItem(
//        icon = R.drawable.ic_bottom_menu_dapps, label = "DApps"
//    )
//)
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun MainScreen(
//    onNavigateTo: (NavigationCommand) -> Unit
//) {
//    val pageState = rememberPagerState()
//    val tabIndex = pageState.currentPage
//    val scope = rememberCoroutineScope()
//    val menus = remember {
//        stateOf
//    }
//
//    val systemUIController = rememberSystemUiController()
//    val useDartIcons = isSystemInDarkTheme()
//    val statusColor = MaterialTheme.colorScheme.primary
//
//    LaunchedEffect(key1 = pageState.currentPage) {
//        systemUIController.setStatusBarColor(
//            statusColor, useDartIcons
//        )
//    }
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        HorizontalPager(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth(),
//            count = menus.size, state = pageState
//        ) { page ->
//            when (page) {
//                0 -> {
//                    // pager 1
//                }
//                1 -> {
//                    // pager 2
//                }
//                else -> Unit
//            }
//        }
//        TabRow(
//            selectedTabIndex = tabIndex,
//            modifier = Modifier.height(56.dp),
//            indicator = {}
//        ) {
//            menus.forEachIndexed { index, bottomMenuItem ->
//                Tab(
//                    selected = index == tabIndex,
//                    selectedContentColor = MaterialTheme.colorScheme.primaryContainer,
//                    unselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
//                    onClick = {
//                        scope.launch {
//                            pageState.animateScrollToPage(index)
//                        }
//                    }
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(
//                            painter = painterResource(id = bottomMenuItem.icon),
//                            contentDescription = null
//                        )
//                        Text(text = bottomMenuItem.label)
//                    }
//                }
//            }
//        }
//    }
//}
//*/
