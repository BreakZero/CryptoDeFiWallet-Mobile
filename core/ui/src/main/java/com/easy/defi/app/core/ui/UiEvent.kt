package com.easy.defi.app.core.ui

sealed class UiEvent {
  object Success : UiEvent()
  object NavigateUp : UiEvent()
  data class ShowSnackbar(val message: UiText) : UiEvent()
}
