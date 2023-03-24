package com.example.moneymanagement.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneymanagement.database.entity.Category
import com.example.moneymanagement.database.entity.Subcategory

data class CategoryWithSubcategories(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val subcategories: List<Subcategory>
)