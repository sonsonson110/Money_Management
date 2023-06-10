package com.example.moneymanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.data.entity.Subcategory
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
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    private val _transactionList = transactionRepository.loadTransactionHomeList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )
    val transactionList = _transactionList
        .combine(searchText) { list, text ->
            if (text.isBlank())
                list
            else
                list.filter { it.doesMatchSearchQuery(text) }
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _transactionList.value
        )

    val categoryWithSubCategoryList =
        categoryWithSubcategoriesRepository.loadAllCategoryWithSubcategory
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = listOf()
            )

    val subcategoryList: StateFlow<List<Subcategory>> = categoryWithSubCategoryList
        .combine(_selectedCategoryChipId) { list, index ->
            if (index != -1)
                list[index].subcategories
            else
                listOf()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )

    val isSubcategoryChipSelected: StateFlow<Boolean> = _selectedSubcategoryChipId
        .map { index -> index >= 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
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
}



