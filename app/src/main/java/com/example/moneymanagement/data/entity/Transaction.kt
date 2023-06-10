package com.example.moneymanagement.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "transaction_table",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("category_id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Subcategory::class,
            parentColumns = arrayOf("subcategory_id"),
            childColumns = arrayOf("subcategory_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("transaction_id")
    val transactionId: Int = 0,
    @ColumnInfo("transaction_name")
    val transactionName: String?,
    @ColumnInfo("transaction_amount")
    val transactionAmount: Double,
    @ColumnInfo("transaction_date")
    val transactionDate: String,
    @ColumnInfo("transaction_note")
    val transactionNote: String?,
    @ColumnInfo("category_id")
    val categoryId: Int,
    @ColumnInfo("subcategory_id")
    val subcategoryId: Int?
)

