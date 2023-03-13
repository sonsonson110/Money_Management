package com.example.moneymanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneymanagement.database.dao.CategoryWithSubcategoriesDao
import com.example.moneymanagement.database.dao.TransactionDao
import com.example.moneymanagement.database.model.*

@Database(
    entities = [Transaction::class, Category::class, Subcategory::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryWithSubcategoriesDao(): CategoryWithSubcategoriesDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java,
                        "app_database"
                    )
                        .createFromAsset("database/money_management.db")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}