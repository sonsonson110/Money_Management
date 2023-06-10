package com.example.moneymanagement.ui.chart

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.ui.DatePicker
import com.example.moneymanagement.data.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.ui.BottomNavigator
import com.example.moneymanagement.ui.detail.groupAmount
import com.example.moneymanagement.ui.home.TransactionsItem
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.patrykandpatrick.vico.core.extension.sumByFloat

object StatDestination : NavigationDestination {
    override val route = "stat_destination"
    override val title = "Thống kê chi tiêu"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemDetail: (Int) -> Unit,
    viewModel: ChartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val groupTransactions: Map<String, Float> = viewModel.groupTransactions.collectAsState().value
    val filteredTransactionList by viewModel.filteredTransactionList.collectAsState()

    StatBody(
        navigateToHomeScreen,
        navigateToItemEntry,
        navigateToItemDetail,
        filteredTransactionList,
        groupTransactions,
        viewModel::onSearchButtonClick
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatBody(
    navigateToHomeScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemDetail: (Int) -> Unit,
    transactionList: List<TransactionWithCateAndSubcategory>,
    groupTransactions: Map<String, Float>,
    onSearchButtonClick: (String, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(StatDestination.title) }
            )
        },
        bottomBar = {
            BottomNavigator(
                navigateToHomeScreen,
                {},
                navigateToItemEntry,
                StatDestination.route
            )
        },
    ) { innerPadding ->

        var state by remember { mutableStateOf(0) }
        var startDateState by remember { mutableStateOf("") }
        var endDateState by remember { mutableStateOf("") }
        val titles = listOf("Thống kê danh sách", "Thống kê biểu đồ")

        Column(
            Modifier.padding(
                12.dp,
                12.dp,
                12.dp,
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(0.7f)) {
                    Text(
                        text = "Ngày bắt đầu",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    DatePicker(
                        transactionDate = startDateState,
                        onValueChange = { startDateState = it }
                    )

                    Text(
                        text = "Ngày kết thúc",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    DatePicker(
                        transactionDate = endDateState,
                        onValueChange = { endDateState = it },
                        limitPastDate = startDateState
                    )
                }

                Spacer(modifier = Modifier.weight(0.05f))

                Button(onClick = { onSearchButtonClick(startDateState, endDateState) }) {
                    Icon(imageVector = Icons.Filled.Search, null)
                }
            }

            TabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = state == index,
                        onClick = { state = index }
                    )
                }
            }
            if (state == 0) {
                Text(
                    text = "Tổng số tiền đã chi: ${
                        groupTransactions.values.sumByFloat { it }.toInt().toString().groupAmount()
                    }đ",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                FilteredTransactionList(
                    transactionList = transactionList,
                    navigateToItemDetail = navigateToItemDetail
                )
            } else {
                CustomChart(groupTransactions = groupTransactions)
            }
        }
    }
}


@Composable
fun FilteredTransactionList(
    transactionList: List<TransactionWithCateAndSubcategory>,
    navigateToItemDetail: (Int) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(transactionList) { index, transac ->
            TransactionsItem(
                transactionWithCateAndSubcategory = transac,
                index = index,
                onItemClick = { navigateToItemDetail(transac.transaction.transactionId) })
        }
    }
}

