package com.example.moneymanagement.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneymanagement.database.entity.Category
import com.example.moneymanagement.database.entity.Subcategory
import com.example.moneymanagement.database.entity.Transaction

data class TransactionWithCateAndSubcategory(
    @Embedded val transaction: Transaction = Transaction(0,"",0.0,"","",0,0),
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
