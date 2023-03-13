package com.example.moneymanagement.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("category_id")
    val categoryId: Int,
    @ColumnInfo("category_name")
    val categoryName: String,
    @ColumnInfo("category_icon_name")
    val categoryIconName: String,
)
