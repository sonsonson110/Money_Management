package com.example.moneymanagement.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneymanagement.data.entity.Category
import com.example.moneymanagement.data.entity.Subcategory
import com.example.moneymanagement.data.entity.Transaction

data class TransactionWithCateAndSubcategory(
    @Embedded
    val transaction: Transaction = Transaction(0,"",0.0,"","",0,0),

    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val category: Category = Category(0,"",""),

    @Relation(
        parentColumn = "subcategory_id",
        entityColumn = "subcategory_id"
    )
    val subcategory: Subcategory? = null
)
