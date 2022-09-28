package com.crypto.defi.feature.transactions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.DeFiBoxWithConstraints
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.feature.transactions.components.TransactionItemView
import com.crypto.defi.feature.transactions.components.TransactionsMotionLayout

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TransactionListPager(
    slug: String,
    txnListViewModel: TransactionListViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    val tnxUiState =
        txnListViewModel.tnxState.collectAsState(initial = TransactionListState())
    val transactionList = tnxUiState.value.transactionList.collectAsLazyPagingItems()
    LaunchedEffect(key1 = slug, block = {
        txnListViewModel.init(slug)
    })
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DeFiAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                navigateUp()
            }
        }) {
        DeFiBoxWithConstraints { progress, isExpanded ->
            TransactionsMotionLayout(
                asset = tnxUiState.value.asset, targetValue = progress
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