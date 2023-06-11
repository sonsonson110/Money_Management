package com.example.moneymanagement.ui

import android.annotation.SuppressLint
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
import com.example.moneymanagement.ui.home.toDisplayDate
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun DatePicker(
    transactionDate: String,
    onValueChange: (String) -> Unit,
    limitPastDate: String? = null
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // lấy dữ liệu ngày tháng năm hiện tại
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val selectedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
            onValueChange(selectedDate)
        }, year, month, dayOfMonth
    )
    limitPastDate?.takeIf { it.isNotBlank() }?.let {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(it)
        datePicker.datePicker.minDate = date!!.time
    }

    TextField(
        value = transactionDate.toDisplayDate(),
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
