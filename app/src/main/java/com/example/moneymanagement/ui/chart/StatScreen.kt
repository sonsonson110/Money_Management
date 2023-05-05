package com.example.moneymanagement.ui.chart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.ui.BottomNavigator
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme

object StatDestination : NavigationDestination {
    override val route = "stat_destination"
    override val title = "stat screen"
}

@Composable
fun StatScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    viewModel: ChartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val transactionMonthSumMap by viewModel.transactionMonthSumMap.collectAsState()

    StatBody(
        navigateToHomeScreen,
        navigateToItemEntry,
        transactionMonthSumMap = transactionMonthSumMap
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatBody(
    navigateToHomeScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    transactionMonthSumMap: Map<String, Float>
) {
    Scaffold(
        bottomBar = { BottomNavigator(navigateToHomeScreen, {}, StatDestination.route) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToItemEntry() }) {
                Icon(Icons.Filled.Add, null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {

        Column(modifier = Modifier.padding(8.dp)) {

            Text(text = "Thống kê chi tiêu")

            CustomChart(transactionMonthSumMap)

        }
    }
}


@Composable
@Preview
fun Preview() {
    MoneyManagementTheme {
        StatScreen({}, {})
    }
}