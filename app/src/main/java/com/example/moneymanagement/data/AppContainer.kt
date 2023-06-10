package com.example.moneymanagement.data

import android.content.Context
import com.example.moneymanagement.data.repository.CategoryWithSubcategoriesRepository
import com.example.moneymanagement.data.repository.TransactionRepository

interface AppContainer {
    val transactionRepository: TransactionRepository
    val categoryWithSubcategoriesRepository: CategoryWithSubcategoriesRepository
}

/**
 * [AppDataContainer] cung cấp [TransactionRepository], [CategoryWithSubcategoriesRepository] và các dữ liệu bên trong repo
 */
class AppDataContainer(private val context: Context): AppContainer {
    override val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(TransactionDatabase.getDatabase(context).transactionDao())
    }
    override val categoryWithSubcategoriesRepository: CategoryWithSubcategoriesRepository by lazy {
        CategoryWithSubcategoriesRepository(TransactionDatabase.getDatabase(context).categoryWithSubcategoriesDao())
    }
}