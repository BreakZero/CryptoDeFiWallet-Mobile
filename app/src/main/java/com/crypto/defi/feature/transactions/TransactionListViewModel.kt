package com.crypto.defi.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.chains.ChainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    val chainRepository: ChainRepository
) : ViewModel() {
    fun init(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chainRepository.assetBySlug(slug).also {
                Timber.v(it.toString())
            }
        }
    }
}