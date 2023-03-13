package com.example.moneymanagement.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.dao.TransactionDetail
import com.example.moneymanagement.database.model.Transaction
import com.example.moneymanagement.database.repository.TransactionRepository
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
        transactionRepository.deleteTransaction(uiState.value.transactionDetail.toTransaction())
    }
}

data class TransactionDetailUiState(
    val transactionDetail: TransactionDetail = TransactionDetail()
)

fun TransactionDetail.toTransaction(): Transaction = this.transaction