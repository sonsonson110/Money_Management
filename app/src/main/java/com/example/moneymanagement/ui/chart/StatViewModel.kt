package com.example.moneymanagement.ui.chart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanagement.data.repository.TransactionRepository
import com.patrykandpatrick.vico.core.extension.sumByFloat
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class ChartViewModel(
    transactionRepository: TransactionRepository
) : ViewModel() {
    //toàn bộ lịch sử giao dịch
    private val transactionList = transactionRepository.loadTransactionHomeList()
    private val selectedStartDate = MutableStateFlow("")
    private val selectedEndDate = MutableStateFlow("")

    @RequiresApi(Build.VERSION_CODES.O)
    val filteredTransactionList = transactionList
        .combine(selectedStartDate) { list, startDate ->
            if (startDate.isEmpty())
                emptyList()
            else
                list.filter {
                    val dDate = LocalDate.parse(it.transaction.transactionDate, formatter)
                    val sDate = LocalDate.parse(startDate, formatter)
                    dDate.isEqual(sDate) || dDate.isAfter(sDate)
                }
        }
        .combine(selectedEndDate) { list, endDate ->
            if (endDate.isEmpty())
                list
            else
                list.filter {
                    val dDate = LocalDate.parse(it.transaction.transactionDate, formatter)
                    val eDate = LocalDate.parse(endDate, formatter)
                    dDate.isEqual(eDate) || dDate.isBefore(eDate)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    @RequiresApi(Build.VERSION_CODES.O)
    val groupTransactions: StateFlow<Map<String, Float>> = filteredTransactionList.map { list ->
            list.groupBy { transaction ->
                val transactionDate = transaction.transaction.transactionDate.toLocalDate()
                val startDate = selectedStartDate.value.toLocalDate()
                val daysBetween = ChronoUnit.DAYS.between(startDate, transactionDate)
                val weekNumber = daysBetween / 7 // Gom nhóm theo số tuần từ ngày bắt đầu

                val startOfWeek = startDate.plusWeeks(weekNumber)
                val endOfWeek = startOfWeek.plusDays(6)
                val weekString = "${startOfWeek.toDayAndMonthString()} - ${endOfWeek.toDayAndMonthString()}"

                weekString
            }.mapValues { (_,transactions) ->
                if (transactions.isEmpty())
                    0f
                else
                    transactions.sumByFloat { it.transaction.transactionAmount.toFloat() }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyMap()
        )

    fun onSearchButtonClick(startDate: String, endDate: String) {
        selectedStartDate.value = startDate
        selectedEndDate.value = endDate
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDayAndMonthString(): String {
    return "${this.dayOfMonth}/${this.monthValue}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(): LocalDate = LocalDate.parse(this)

@RequiresApi(Build.VERSION_CODES.O)
private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

