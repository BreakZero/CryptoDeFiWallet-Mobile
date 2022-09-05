package com.crypto.defi.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.wallet.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wallet.core.jni.HDWallet
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {
    private val _uiEvent = Channel<Boolean>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            walletRepository.activeOne()?.let {
                try {
                    walletRepository.inject(HDWallet(it.mnemonic, it.passphrase))
                    _uiEvent.send(true)
                } catch (e: Exception) {
                    _uiEvent.send(false)
                }
            } ?: kotlin.run {
                _uiEvent.send(false)
            }
        }
    }
}
