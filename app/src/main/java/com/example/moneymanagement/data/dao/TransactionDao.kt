package com.example.moneymanagement.data.dao

import androidx.room.*
import com.example.moneymanagement.data.entity.*
import com.example.moneymanagement.data.entity.Transaction
import com.example.moneymanagement.data.model.TransactionWithCateAndSubcategory
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
