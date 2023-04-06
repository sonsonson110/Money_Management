package com.example.moneymanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.entity.Subcategory
import com.example.moneymanagement.database.repository.CategoryWithSubcategoriesRepository
import com.example.moneymanagement.database.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    transactionRepository: TransactionRepository,
    categoryWithSubcategoriesRepository: CategoryWithSubcategoriesRepository
) : ViewModel() {
    /*
    * Filtering
    */
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //truy cập category chip thông qua chỉ số của danh sách category chip
    private val _selectedCategoryChipIndex = MutableStateFlow(-1)
    val selectedCategoryChipIndex = _selectedCategoryChipIndex.asStateFlow()

    //truy cập subcategory chip thông qua id của nó
    private val _selectedSubcategoryChipId = MutableStateFlow(-1)
    val selectedSubcategoryChipId = _selectedSubcategoryChipId.asStateFlow()

    val isCategoryChipSelected: StateFlow<Boolean> = _selectedCategoryChipIndex
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
        .combine(_selectedCategoryChipIndex) { list, index ->
            if (index == -1)
                list
            else
                list.filter { it.category.categoryId == index.inc() }
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
        .combine(_selectedCategoryChipIndex) { list, index ->
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

    fun onCategoryChipChange(index: Int) {
        _selectedCategoryChipIndex.value = index
    }

    fun onSubcategoryChipChange(id: Int) {
        _selectedSubcategoryChipId.value = id
    }
}


