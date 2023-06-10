package com.example.moneymanagement.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneymanagement.data.entity.Category
import com.example.moneymanagement.data.entity.Subcategory

data class CategoryWithSubcategories(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val subcategories: List<Subcategory>
)