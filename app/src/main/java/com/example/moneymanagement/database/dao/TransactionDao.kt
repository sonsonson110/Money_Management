package com.example.moneymanagement.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.moneymanagement.database.model.*
import com.example.moneymanagement.database.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TransactionDao {
    @RawQuery(observedEntities = [Transaction::class, Category::class])
    fun loadTransactionHomeList(query: SupportSQLiteQuery): Flow<List<TransactionHome>>

    //Định nghĩa truy vấn với kiểu trả về tuỳ chọn
    @RawQuery(observedEntities = [Transaction::class, Category::class, Subcategory::class])
    fun loadTransactionDetailById(query: SupportSQLiteQuery): Flow<TransactionDetail>

    //bộ 3 insert một giao dịch hoàn chỉnh vào cơ sở dữ liệu
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}


//Định nghĩa kiểu dữ liệu trả về cho RawQuery
//Set sẵn default value để hỗ trợ stateflow và làm placeholder cho compose ui
data class TransactionDetail(
    @Embedded val transaction: Transaction =
        Transaction(
            0,
            "",
            0.0,
            "",
            "",
            0,
            0
        ),
    @ColumnInfo("category_name")
    val categoryName: String = "",
    @ColumnInfo("category_icon_name")
    val categoryIconName: String = "",
    @ColumnInfo("subcategory_name")
    val subcategoryName: String? = ""
)

data class TransactionHome(
    @Embedded val transaction: Transaction =
        Transaction(
            0,
            "",
            0.0,
            "",
            "",
            0,
            0
        ),
    @ColumnInfo("category_icon_name")
    val categoryIconName: String = ""
)