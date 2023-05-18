package com.example.moneymanagement.database

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.moneymanagement.R
import com.example.moneymanagement.ui.entry.TransactionEntry
import com.example.moneymanagement.ui.home.toFormattedDate
import java.util.*

@Composable
fun DatePicker(
    transactionDate: String,
    transactionEntry: TransactionEntry,
    onValueChange: (TransactionEntry) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val selectedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
            onValueChange(transactionEntry.copy(transactionDate = selectedDate))
        }, year, month, dayOfMonth
    )

    TextField(
        value = transactionDate.toFormattedDate(),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(
                painterResource(id = R.drawable.calendar_icon),
                contentDescription = null,
                modifier = Modifier.clickable {
                    datePicker.show()
                }
            )
        }
    )
}
