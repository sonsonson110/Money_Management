package com.example.moneymanagement.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subcategory",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("category_id"),
        childColumns = arrayOf("category_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Subcategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("subcategory_id")
    val subcategoryId: Int,
    @ColumnInfo("subcategory_name")
    val subcategoryName: String,
    @ColumnInfo("category_id")
    val categoryId : Int,
)
