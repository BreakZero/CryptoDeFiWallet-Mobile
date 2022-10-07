package com.crypto.defi.di

import com.crypto.defi.feature.assets.send.SendFormViewModel
import com.crypto.defi.feature.assets.transactions.TransactionListViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
  fun sendFormAssistedViewModelFactory(): SendFormAssistedFactory
  fun transactionListAssistedViewModelFactory(): TransactionListAssistedFactory
}

@dagger.assisted.AssistedFactory
interface SendFormAssistedFactory {
  fun createSendFormViewModel(slug: String): SendFormViewModel
}

@dagger.assisted.AssistedFactory
interface TransactionListAssistedFactory {
  fun createTransactionListViewModel(slug: String): TransactionListViewModel
}



