package com.crypto.defi.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.defi.chains.ChainManager
import com.crypto.wallet.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import wallet.core.jni.HDWallet
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val chainManager: ChainManager,
  private val walletRepository: WalletRepository
) : ViewModel() {
  init {
    viewModelScope.launch(Dispatchers.IO) {
      walletRepository.activeOne()?.let {
        try {
          chainManager.fetchingChains(HDWallet(it.mnemonic, it.passphrase))
        } catch (e: Exception) {
          // not wallet, restart, reset passcode
          Timber.e(e)
        }
      } ?: kotlin.run {
        // not wallet, restart, reset passcode
      }
    }
  }
}
