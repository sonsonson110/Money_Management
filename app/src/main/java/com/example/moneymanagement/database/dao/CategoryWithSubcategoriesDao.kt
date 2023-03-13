package com.example.moneymanagement.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.moneymanagement.database.model.CategoryWithSubcategories
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryWithSubcategoriesDao {
    //Dùng trong phần entry/insert
    @Transaction
    @Query("SELECT * FROM category")
    fun loadAllCategoryWithSubcategory(): Flow<List<CategoryWithSubcategories>>
}