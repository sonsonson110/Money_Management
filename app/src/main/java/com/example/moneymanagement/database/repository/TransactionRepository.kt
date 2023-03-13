package com.example.moneymanagement.database.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.moneymanagement.database.dao.TransactionDao
import com.example.moneymanagement.database.dao.TransactionDetail
import com.example.moneymanagement.database.dao.TransactionHome
import com.example.moneymanagement.database.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)

    /**
     * Truy vấn trả về toàn bộ danh sách giao dịch-thể loại cha của giao dịch
     * dành cho Home Screen
     */
    fun loadTransactionHomeList(): Flow<List<TransactionHome>> =
        transactionDao.loadTransactionHomeList(
            SimpleSQLiteQuery(
                query = "SELECT t.*, c.category_icon_name\n" +
                        "FROM transaction_table t\n" +
                        "LEFT JOIN category c ON t.category_id = c.category_id\n"
            )
        )

    /**
     * Truy vấn trả về toàn bộ danh sách giao dịch cùng với tên của thể loại (cả thể loại con) của giao dịch
     * dành cho Detail Screen
     */
    fun loadTransactionDetailById(transactionId: Int): Flow<TransactionDetail> =
        transactionDao.loadTransactionDetailById(
            SimpleSQLiteQuery(
                query = "SELECT t.*, c.category_name, c.category_icon_name, s.subcategory_name\n" +
                        "FROM transaction_table t\n" +
                        "LEFT JOIN category c ON t.category_id = c.category_id\n" +
                        "LEFT JOIN subcategory s ON t.subcategory_id = s.subcategory_id\n" +
                        "WHERE t.transaction_id = ?",
                bindArgs = arrayOf(transactionId)
            )
        )
}