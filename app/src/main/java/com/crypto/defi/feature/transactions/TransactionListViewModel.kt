package com.crypto.defi.feature.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.crypto.core.extensions.launchWithHandler
import com.crypto.defi.chains.ChainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val chainRepository: ChainRepository,
    val client: HttpClient,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun init(slug: String) {
        if (savedStateHandle.get<String>("slug") == slug) return;
        savedStateHandle["slug"] = slug
    }

    private val _slugState = savedStateHandle.getStateFlow("slug", "")

    val tnxState = _slugState.map {
        val asset = chainRepository.assetBySlug(it)
        asset?.let {
            val address = chainRepository.getChainByKey(it.code).address()
            TransactionListState(
                asset = asset,
                transactionList = Pager(PagingConfig(pageSize = 20)) {
                    TransactionListSource(
                        client = client,
                        contractAddress = asset.contract,
                        address = address
                    )
                }.flow.cachedIn(viewModelScope)
            )
        } ?: TransactionListState(
            asset = null,
            transactionList = flow { emit(PagingData.empty()) }
        )
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())
}