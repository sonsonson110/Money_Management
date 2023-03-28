package com.example.moneymanagement.database.repository

import com.example.moneymanagement.database.dao.TransactionDao
import com.example.moneymanagement.database.entity.Transaction
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)

    /**
     * Truy vấn trả về toàn bộ danh sách giao dịch-thể loại cha của giao dịch
     * dành cho Home Screen
     */
    fun loadTransactionHomeList(): Flow<List<TransactionWithCateAndSubcategory>> = transactionDao.loadTransactionHomeList()
    /**
     * Truy vấn trả về toàn bộ danh sách giao dịch cùng với tên của thể loại (cả thể loại con) của giao dịch
     * dành cho Detail Screen
     */
    fun loadTransactionDetailById(transactionId: Int): Flow<TransactionWithCateAndSubcategory> = transactionDao.loadTransactionDetailById(transactionId)
}