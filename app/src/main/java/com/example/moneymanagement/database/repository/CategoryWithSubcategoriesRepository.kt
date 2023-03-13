package com.example.moneymanagement.database.repository

import com.example.moneymanagement.database.dao.CategoryWithSubcategoriesDao
import com.example.moneymanagement.database.model.CategoryWithSubcategories
import kotlinx.coroutines.flow.Flow

class CategoryWithSubcategoriesRepository(categoryWithSubcategoriesDao: CategoryWithSubcategoriesDao) {
    val loadAllCategoryWithSubcategory:
            Flow<List<CategoryWithSubcategories>> = categoryWithSubcategoriesDao.loadAllCategoryWithSubcategory()
}