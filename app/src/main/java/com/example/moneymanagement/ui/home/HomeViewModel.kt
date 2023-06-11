package com.example.moneymanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.data.entity.Category
import com.example.moneymanagement.data.entity.Subcategory
import com.example.moneymanagement.data.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.data.repository.CategoryWithSubcategoriesRepository
import com.example.moneymanagement.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    transactionRepository: TransactionRepository,
    categoryWithSubcategoriesRepository: CategoryWithSubcategoriesRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedCategoryChipId = MutableStateFlow(-1)
    val selectedCategoryChipId = _selectedCategoryChipId.asStateFlow()

    private val _selectedSubcategoryChipId = MutableStateFlow(-1)
    val selectedSubcategoryChipId = _selectedSubcategoryChipId.asStateFlow()

    val isCategoryChipSelected: StateFlow<Boolean> = _selectedCategoryChipId
        .map { index -> index >= 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT),
            initialValue = false
        )

    private val _transactionList = transactionRepository.loadTransactionHomeList()

    val transactionsGroupByMonthOfYear: StateFlow<Map<String, List<TransactionWithCateAndSubcategory>>> =
        _transactionList
            .combine(searchText) { list, text ->
                if (text.isBlank())
                    list
                else
                    list.filter { it.transaction.doesMatchSearchQuery(text) }
            }
            .combine(_selectedCategoryChipId) { list, id ->
                if (id == -1)
                    list
                else
                    list.filter { it.category.categoryId == id }
            }
            .combine(_selectedSubcategoryChipId) { list, id ->
                if (id == -1)
                    list
                else
                    list.filter { it.subcategory?.subcategoryId == id }
            }
                //chuyển đổi list sang map với key là các tháng trong năm
            .map { transactionList ->
                transactionList.groupBy {
                    val monthValue = it.transaction.transactionDate.subSequence(0, 7).toString()
                    monthValue
                }.mapValues { (_, transactions) ->
                    val resortedTransactionList = mutableListOf<TransactionWithCateAndSubcategory>()
                    var i = 0
                    while (i < transactions.size) {
                        val currentTransaction = transactions[i]
                        var endIndex = i
                        val transactionsWithSameDate = mutableListOf(currentTransaction)
                        while (endIndex + 1 < transactions.size && transactions[endIndex + 1].transaction.transactionDate == currentTransaction.transaction.transactionDate) {
                            transactionsWithSameDate.add(transactions[++endIndex])
                        }
                        transactionsWithSameDate.sortByDescending { it.transaction.transactionId }
                        resortedTransactionList.addAll(transactionsWithSameDate)
                        i = endIndex + 1
                    }
                    resortedTransactionList
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT),
                initialValue = emptyMap()
            )

    private val categoryWithSubCategoryList = categoryWithSubcategoriesRepository.loadAllCategoryWithSubcategory

    val categoryList : StateFlow<List<Category>> = categoryWithSubCategoryList
        .map { originList -> originList.map { it.category } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT),
            initialValue = listOf()
        )


    val subcategoryList: StateFlow<List<Subcategory>> = categoryWithSubCategoryList
        .combine(_selectedCategoryChipId) { list, id ->
            if (id != -1)
                list.first { it.category.categoryId == id }.subcategories
            else
                listOf()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT),
            initialValue = listOf()
        )

    val isSubcategoryChipSelected: StateFlow<Boolean> = _selectedSubcategoryChipId
        .map { index -> index >= 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIBE_TIMEOUT),
            initialValue = false
        )


    fun onSearchFieldChange(text: String) {
        _searchText.value = text
    }

    fun onCategoryChipChange(id: Int) {
        _selectedCategoryChipId.value = id
    }

    fun onSubcategoryChipChange(id: Int) {
        _selectedSubcategoryChipId.value = id
    }

    companion object {
        const val SUBSCRIBE_TIMEOUT = 5000L
    }
}



