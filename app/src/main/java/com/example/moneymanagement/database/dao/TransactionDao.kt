package com.example.moneymanagement.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.moneymanagement.database.entity.*
import com.example.moneymanagement.database.entity.Transaction
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TransactionDao {
    @androidx.room.Transaction
    @Query("SELECT * FROM transaction_table ORDER BY transaction_date DESC")
    fun loadTransactionHomeList(): Flow<List<TransactionWithCateAndSubcategory>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transaction_table WHERE transaction_id = :transactionId")
    fun loadTransactionDetailById(transactionId: Int): Flow<TransactionWithCateAndSubcategory>
    //bộ 3 insert một giao dịch hoàn chỉnh vào cơ sở dữ liệu
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)
}
