package com.example.moneymanagement.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.data.entity.Transaction
import com.example.moneymanagement.data.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class TransactionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    private val transactionId: Int = checkNotNull(savedStateHandle[TransactionDetailDestination.transactionIdArg])

    val uiState: StateFlow<TransactionDetailUiState> =
        transactionRepository.loadTransactionDetailById(transactionId)
            .filterNotNull()
            .map {
                TransactionDetailUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TransactionDetailUiState()
            )

    suspend fun deleteTransaction() {
        transactionRepository.deleteTransaction(uiState.value.transactionWithCateAndSubcategory.toTransaction())
    }
}

data class TransactionDetailUiState(
    val transactionWithCateAndSubcategory: TransactionWithCateAndSubcategory = TransactionWithCateAndSubcategory()
)

fun TransactionWithCateAndSubcategory.toTransaction(): Transaction = this.transaction