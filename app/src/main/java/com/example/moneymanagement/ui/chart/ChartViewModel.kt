package com.example.moneymanagement.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.database.repository.TransactionRepository
import com.patrykandpatrick.vico.core.extension.sumByFloat
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class ChartViewModel(
    transactionRepository: TransactionRepository
) : ViewModel() {
    //toàn bộ lịch sử giao dịch
    private val transactionList = transactionRepository.loadTransactionHomeList()

    //lọc danh sách giao dịch với các thuộc tính nào thuộc 3-4 tháng trc hay k
    private val filteredMonthsTransactionList = transactionList
        .map { list ->
            list.filter {
                val transactionDate =
                    dateFormat.parse(it.transaction.transactionDate)
                transactionDate!!.after(threeMonthsAgo) || transactionDate.equals(threeMonthsAgo)
            } // Filter the list based on the transaction date
        }
    //lấy danh sách giao dịch trong các 5-6 gần nhất
    private val filteredWeeksTransactionList = transactionList
        .map { list ->
            list.filter {
                val transactionDate =
                    dateFormat.parse(it.transaction.transactionDate)
                transactionDate!!.after(fourWeeksAgo) || transactionDate.equals(fourWeeksAgo)
            }
        }

    //dữ liệu cho chart
    //map key theo tháng và tổng tiền từng tháng
    val transactionMonthSumMap: StateFlow<Map<String, Float>> = filteredMonthsTransactionList
        .map { list ->
            list.groupBy {
                // Extract the month name from the transaction date
                val date = dateFormat.parse(it.transaction.transactionDate)
                monthFormat.format(date!!)
            }.mapValues { (_, transactions) ->
                // Calculate the total transaction amount for this month
                if (transactions.isEmpty())
                    0f
                else
                    transactions.sumByFloat { it.transaction.transactionAmount.toFloat() }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyMap()
        )

    //map tuần và tổng tiền từng tuần
    val transactionWeekSumMap: StateFlow<Map<String, Float>> = filteredWeeksTransactionList
        .map { list ->
            list.groupBy {
                // Extract the month name from the transaction date
                val date = dateFormat.parse(it.transaction.transactionDate)
                weekFormat.format(date!!)
            }.mapValues { (_, transactions) ->
                // Calculate the total transaction amount for this month
                if (transactions.isEmpty())
                    0f
                else
                    transactions.sumByFloat { it.transaction.transactionAmount.toFloat() }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyMap()
        )
}

//For calendar
private val calendar1 = Calendar.getInstance()
private val calendar2 = Calendar.getInstance()
private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
private val weekFormat = SimpleDateFormat("W-MMM")
private val monthFormat = SimpleDateFormat("MMMM")
private val threeMonthsAgo = calendar1.apply {
    add(Calendar.MONTH, -3)
}.time // Calculate the date three months ago
val fourWeeksAgo = calendar2.apply {
    add(Calendar.WEEK_OF_YEAR, -4)
}.time // Calculate the date four weeks ago