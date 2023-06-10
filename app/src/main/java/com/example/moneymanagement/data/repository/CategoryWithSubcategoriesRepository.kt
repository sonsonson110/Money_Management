package com.example.moneymanagement.data.repository

import com.example.moneymanagement.data.dao.CategoryWithSubcategoriesDao
import com.example.moneymanagement.data.model.CategoryWithSubcategories
import kotlinx.coroutines.flow.Flow

class CategoryWithSubcategoriesRepository(categoryWithSubcategoriesDao: CategoryWithSubcategoriesDao) {
    val loadAllCategoryWithSubcategory:
            Flow<List<CategoryWithSubcategories>> = categoryWithSubcategoriesDao.loadAllCategoryWithSubcategory()
}