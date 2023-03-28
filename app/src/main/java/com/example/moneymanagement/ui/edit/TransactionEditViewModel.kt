package com.example.moneymanagement.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.database.repository.CategoryWithSubcategoriesRepository
import com.example.moneymanagement.database.repository.TransactionRepository
import com.example.moneymanagement.ui.detail.groupAmount
import com.example.moneymanagement.ui.entry.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class TransactionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
    categoryWithSubcategoriesRepository: CategoryWithSubcategoriesRepository
): ViewModel() {

    //đưa ra màn hình sub-category list
    val categoryState: StateFlow<CategoryState> =
        categoryWithSubcategoriesRepository.loadAllCategoryWithSubcategory.map {
            CategoryState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CategoryState()
        )

    //Edit screen state
    var transactionEditUiState by mutableStateOf(TransactionEntryUiState())
        private set

    private val transactionId: Int = checkNotNull(savedStateHandle[TransactionEditDestination.transactionIdArg])

    init {
        viewModelScope.launch {
            transactionEditUiState = transactionRepository.loadTransactionDetailById(transactionId)
                .filterNotNull()
                .first()
                .toTransactionEntryUiState(true)
        }
    }

    fun updateUiState(transactionEntry: TransactionEntry) {
        transactionEditUiState = TransactionEntryUiState(
            transactionEntry = transactionEntry,
            isEntryValid = transactionEntry.isValid()
        )
    }

    suspend fun updateTransaction() {
        if (transactionEditUiState.isEntryValid)
            transactionRepository.updateTransaction(transactionEditUiState.transactionEntry.toTransaction())
    }
}

fun TransactionWithCateAndSubcategory.toTransactionEntry() = TransactionEntry(
    transactionId =  transaction.transactionId,
    transactionName = transaction.transactionName?: "Chưa đề cập",
    transactionAmount = DecimalFormat("#").format(transaction.transactionAmount),
    transactionDate = transaction.transactionDate,
    transactionNote = transaction.transactionNote?: "Chưa đề cập",
    category = category,
    subcategory = subcategory
)

fun TransactionWithCateAndSubcategory.toTransactionEntryUiState(isEntryValid: Boolean) = TransactionEntryUiState(
    transactionEntry = this.toTransactionEntry(),
    isEntryValid = isEntryValid
)