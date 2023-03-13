package com.example.moneymanagement.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithSubcategories(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val subcategories: List<Subcategory>
)