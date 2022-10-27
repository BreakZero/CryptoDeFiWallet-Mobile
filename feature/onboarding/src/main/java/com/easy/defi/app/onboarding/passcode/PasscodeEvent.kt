package com.easy.defi.app.onboarding.passcode

sealed class PasscodeEvent {
  data class Insert(val number: String) : PasscodeEvent()
  object Delete : PasscodeEvent()
}
