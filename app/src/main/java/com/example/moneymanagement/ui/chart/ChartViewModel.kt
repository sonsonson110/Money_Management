package com.example.moneymanagement.ui.chart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
    //AND lọc danh sách giao dịch xem có thuộc tính nào thuộc 3 tháng trc hay k
    private val filteredTransactionList = transactionRepository.loadTransactionHomeList()
        .map { list ->
            list.filter {
                val transactionDate =
                    dateFormat.parse(it.transaction.transactionDate)
                transactionDate!!.after(threeMonthsAgo) || transactionDate.equals(threeMonthsAgo)
            } // Filter the list based on the transaction date
        }

    //dữ liệu cho chart
    //tên 3 tháng trước có giao dịch
    val transactionMonthSumMap: StateFlow<Map<String, Float>> = filteredTransactionList
        .map { list ->
            list.groupBy {
                // Extract the month name from the transaction date
                val date = dateFormat.parse(it.transaction.transactionDate)
                monthFormat.format(date!!)
            }.mapValues { (_, transactions) ->
                // Calculate the total transaction amount for this month
                transactions.sumByFloat { it.transaction.transactionAmount.toFloat() }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyMap()
        )
}

//Lấy 3 tháng gấn nhất theo lịch hiện tại
private val calendar = Calendar.getInstance()
private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
private val monthFormat = SimpleDateFormat("MMMM")
private val threeMonthsAgo = calendar.apply {
    add(Calendar.MONTH, -3)
}.time // Calculate the date three months ago