package com.example.moneymanagement

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moneymanagement.ui.detail.TransactionDetailViewModel
import com.example.moneymanagement.ui.entry.TransactionEntryScreenViewModel
import com.example.moneymanagement.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(appApplication().container.transactionRepository)
        }

        initializer {
            TransactionDetailViewModel(
                this.createSavedStateHandle(),
                appApplication().container.transactionRepository,
            )
        }

        initializer {
            TransactionEntryScreenViewModel(
                appApplication().container.transactionRepository,
                appApplication().container.categoryWithSubcategoriesRepository,
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [AppApplication].
 */
fun CreationExtras.appApplication(): AppApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AppApplication)