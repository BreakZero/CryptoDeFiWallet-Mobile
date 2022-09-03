package com.crypto.onboarding.presentation.passcode

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.common.SecurityUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val securityUtil: SecurityUtil
) : ViewModel() {
    var passcodeState by mutableStateOf(PasscodeState(messageLabel = "Enter passcode"))
    private val _uiEvent = Channel<Result<String>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PasscodeEvent) {
        when (event) {
            is PasscodeEvent.Delete -> {
                passcodeState = passcodeState.delete()
            }
            is PasscodeEvent.Insert -> {
                passcodeState = passcodeState.insert(event.number)
                if (passcodeState.passcode.length == 6) {
                    if (passcodeState.originPasscode.isNotEmpty()) {
                        if (passcodeState.originPasscode == passcodeState.passcode) {
                            viewModelScope.launch {
                                _uiEvent.send(Result.success(passcodeState.passcode))
                            }
                        } else {
                            passcodeState = passcodeState.clear("Enter passcode", "not match")
                        }
                    } else {
                        passcodeState = passcodeState.originDone("Enter new passcode")
                    }
                }
            }
            is PasscodeEvent.Done -> {
                preferences.edit {
                    /*val encryptedValue = securityUtil.encryptData(
                        "passcode-alias", passcodeState.passcode
                    )*/
                    putString("passcode", passcodeState.passcode)
                }
            }
        }
    }
}
