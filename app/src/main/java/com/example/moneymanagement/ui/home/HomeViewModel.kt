package com.example.moneymanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.database.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    transactionRepository: TransactionRepository
) : ViewModel() {

    /**
     * homeUiState holds the state of UI. List of transactions received are mapped to homeUiState
     */
    val homeUiState: StateFlow<HomeUiState> =
        transactionRepository.loadTransactionHomeList().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val transactionHomeList: List<TransactionWithCateAndSubcategory> = emptyList()
)
