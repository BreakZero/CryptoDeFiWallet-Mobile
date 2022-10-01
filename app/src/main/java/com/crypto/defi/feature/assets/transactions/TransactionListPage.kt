package com.crypto.defi.feature.assets.transactions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.DeFiBoxWithConstraints
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.core.ui.utils.QRCodeEncoder
import com.crypto.defi.feature.assets.transactions.components.TransactionItemView
import com.crypto.defi.feature.assets.transactions.components.TransactionsMotionLayout
import com.crypto.defi.navigations.SendFormNavigation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TransactionListPager(
    slug: String,
    txnListViewModel: TransactionListViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    val txnUiState = txnListViewModel.txnState
    val transactionList = txnUiState.transactionList.collectAsLazyPagingItems()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = slug, block = {
        txnListViewModel.init(slug)
    })
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DeFiAppBar {
                navigateUp()
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            QRCodeContent(content = txnUiState.address)
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(
            topEnd = MaterialTheme.Spacing.space24,
            topStart = MaterialTheme.Spacing.space24
        )
    ) {
        DeFiBoxWithConstraints { progress, _ ->
            TransactionsMotionLayout(
                asset = txnUiState.asset, targetValue = progress,
                onSend = {
                    navigateTo.invoke(SendFormNavigation.destination(slug))
                },
                onReceive = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                }
            ) {
                AnimatedContent(targetState = true, transitionSpec = {
                    fadeIn(animationSpec = tween(300, 300)) with fadeOut(
                        animationSpec = tween(
                            300,
                            300
                        )
                    )
                }) {
                    LazyColumn(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topEnd = MaterialTheme.Spacing.space24,
                                    topStart = MaterialTheme.Spacing.space24
                                )
                            )
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        contentPadding = PaddingValues(
                            vertical = MaterialTheme.Spacing.medium,
                            horizontal = MaterialTheme.Spacing.small
                        ),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.Spacing.small)
                    ) {
                        when (transactionList.loadState.refresh) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "somethings went wrong")
                                    }
                                }
                            }
                            else -> Unit
                        }
                        items(transactionList) {
                            it?.let {
                                TransactionItemView(data = it)
                            }
                        }
                        if (transactionList.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QRCodeContent(
    content: String
) {
    val image = QRCodeEncoder.encodeQRCode(content)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = content,
                modifier = Modifier.padding(MaterialTheme.Spacing.space12),
                textAlign = TextAlign.Center
            )
            image?.also {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
            }
            Divider(modifier = Modifier.padding(top = MaterialTheme.Spacing.space12))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                    }
            ) {
                Text(
                    text = "Copy",
                    lineHeight = 48.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}