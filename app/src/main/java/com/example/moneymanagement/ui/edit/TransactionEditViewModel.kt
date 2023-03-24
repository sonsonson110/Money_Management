package com.example.moneymanagement.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.repository.CategoryWithSubcategoriesRepository
import com.example.moneymanagement.database.repository.TransactionRepository
import com.example.moneymanagement.ui.entry.CategoryState
import com.example.moneymanagement.ui.entry.TransactionEntryUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionEditViewModel(
    savedStateHandle: SavedStateHandle,
    transactionRepository: TransactionRepository,
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
    var transationEditUiState by mutableStateOf(TransactionEntryUiState())
        private set

    private val transactionId: Int = checkNotNull(savedStateHandle[TransactionEditDestination.transactionIdArg])

//    init {
//        viewModelScope.launch {
//            transactionEditUiState =
//        }
//    }
}