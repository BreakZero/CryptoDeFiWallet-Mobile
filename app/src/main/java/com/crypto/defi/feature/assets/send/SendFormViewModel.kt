package com.crypto.defi.feature.assets.send

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

class SendFormViewModel @AssistedInject constructor(
    @Assisted private val slug: String
): ViewModel() {
    init {
        Timber.tag("=====").v(slug)
    }
    fun onEvent() {

    }
}