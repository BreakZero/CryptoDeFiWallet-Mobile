package com.crypto.defi.feature.transactions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.crypto.core.ui.Spacing
import com.crypto.core.ui.composables.DeFiAppBar
import com.crypto.core.ui.composables.DeFiBoxWithConstraints
import com.crypto.core.ui.routers.NavigationCommand
import com.crypto.defi.feature.transactions.components.TransactionsMotionLayout

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TransactionListPager(
    slug: String,
    txnListViewModel: TransactionListViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateTo: (NavigationCommand) -> Unit
) {
    val transactionState = txnListViewModel.tnxState.collectAsState(initial = TransactionListState())
    val transactionList = transactionState.value.transactionList.collectAsLazyPagingItems()
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
            TransactionsMotionLayout(targetValue = progress) {
                AnimatedContent(targetState = true, transitionSpec = {
                    fadeIn(animationSpec = tween(300, 300)) with fadeOut(
                        animationSpec = tween(
                            300,
                            300
                        )
                    )
                }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    topEnd = MaterialTheme.Spacing.space24,
                                    topStart = MaterialTheme.Spacing.space24
                                )
                            )
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyColumn {
                            items(transactionList) {
                                Text(text = it?.hash.orEmpty())
                            }
                        }
                    }
                }
            }
        }
    }
}