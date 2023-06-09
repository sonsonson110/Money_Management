package com.example.moneymanagement.ui.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.data.entity.Category
import com.example.moneymanagement.data.model.CategoryWithSubcategories
import com.example.moneymanagement.data.entity.Subcategory
import com.example.moneymanagement.data.entity.Transaction
import com.example.moneymanagement.data.repository.CategoryWithSubcategoriesRepository
import com.example.moneymanagement.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class TransactionEntryScreenViewModel(
    private val transactionRepository: TransactionRepository,
    categoryWithSubcategoriesRepository: CategoryWithSubcategoriesRepository,
) : ViewModel() {

    //đưa ra màn hình sub-category list
    val categoryList: StateFlow<List<CategoryWithSubcategories>> =
        categoryWithSubcategoriesRepository.loadAllCategoryWithSubcategory
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf()
            )

    //theo dõi state input
    var transactionEntryUiState by mutableStateOf(TransactionEntryUiState())
        private set

    fun updateUiState(transactionEntry: TransactionEntry) {
        transactionEntryUiState =
            TransactionEntryUiState(
                transactionEntry = transactionEntry,
                isEntryValid = transactionEntry.isValid(),
                currentSelectedCategoryId = transactionEntry.category.categoryId
            )
    }

    suspend fun saveTransaction() {
        if (transactionEntryUiState.isEntryValid)
            transactionRepository.insertTransaction(transactionEntryUiState.transactionEntry.toTransaction())
    }
}

data class TransactionEntryUiState(
    val transactionEntry: TransactionEntry = TransactionEntry(),
    val isEntryValid: Boolean = false,
    val currentSelectedCategoryId: Int = 0
)

data class TransactionEntry(
    val transactionId: Int = 0,
    val transactionName: String = "",
    val transactionAmount: String = "",
    val transactionDate: String = "",
    val transactionNote: String = "",
    val category: Category = Category(-1, "", ""),
    val subcategory: Subcategory? = null,
)
fun isNumericAndPositive(input: String): Boolean {
    val numFromString = input.toDoubleOrNull() ?: return false
    return numFromString > 0
}

fun TransactionEntry.isValid(): Boolean {
    this.let {
        if (it.transactionAmount.isBlank()
            || it.transactionDate.isBlank()
            || it.category.categoryId < 1
            || !isNumericAndPositive(transactionAmount)
        )
            return false
    }
    return true
}

fun TransactionEntry.toTransaction(): Transaction = Transaction(
    transactionId = transactionId,
    transactionName = transactionName.takeIf { it.isNotBlank() },
    transactionDate = transactionDate,
    transactionNote = transactionNote.takeIf { it.isNotBlank() },
    transactionAmount = transactionAmount.toDouble(),
    categoryId = category.categoryId,
    subcategoryId = subcategory?.subcategoryId
)