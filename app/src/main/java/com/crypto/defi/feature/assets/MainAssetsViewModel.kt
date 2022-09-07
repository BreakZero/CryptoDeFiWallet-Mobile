package com.crypto.defi.feature.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.chains.ChainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAssetsViewModel @Inject constructor(
    private val chainRepository: ChainRepository
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            chainRepository.fetching()
        }
    }
}
