package com.crypto.defi.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.wallet.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wallet.core.jni.HDWallet
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.activeOne()?.let {
                Log.d("=====", it.mnemonic)
                try {
                    walletRepository.inject(HDWallet(it.mnemonic, it.passphrase))
                } catch (e: Exception) {
                    // not wallet, restart, reset passcode
                }
            } ?: kotlin.run {
                // not wallet, restart, reset passcode
            }
        }
    }
}
