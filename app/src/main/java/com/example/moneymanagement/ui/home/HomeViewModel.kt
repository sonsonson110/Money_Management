package com.example.moneymanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.database.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    transactionRepository: TransactionRepository
) : ViewModel() {



    /**
     * Logic của thanh tìm kiếm
     */
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    /**
     * Danh sách lịch sử giao dịch lấy từ CSDL
     */
    private val _transactionList = transactionRepository.loadTransactionHomeList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf()
    )
    val transactionList = searchText.combine(_transactionList) { text, list ->
        if (text.isBlank())
            list
        else
            list.filter { it.doesMatchSearchQuery(text) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _transactionList.value
    )

    fun onSearchFieldChange(text: String) {
        _searchText.value = text
    }
}


