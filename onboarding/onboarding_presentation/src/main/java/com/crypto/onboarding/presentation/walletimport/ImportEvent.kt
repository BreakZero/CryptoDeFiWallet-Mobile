package com.crypto.onboarding.presentation.walletimport

sealed class ImportEvent {
  data class OnImportClick(val passcode: String) : ImportEvent()
  data class OnPhraseChange(val phrase: String) : ImportEvent()
  data class OnFocusChange(val isFocused: Boolean) : ImportEvent()
}
