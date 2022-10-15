package com.crypto.onboarding.presentation.passcode

sealed class PasscodeEvent {
  data class Insert(val number: String) : PasscodeEvent()
  object Delete : PasscodeEvent()
}
