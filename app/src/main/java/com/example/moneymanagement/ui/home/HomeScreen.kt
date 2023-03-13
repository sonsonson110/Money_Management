package com.example.moneymanagement.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.database.dao.TransactionHome
import com.example.moneymanagement.database.model.Transaction
import com.example.moneymanagement.ui.detail.groupAmount
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Home Screen"
}

@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        TransactionsList(
            transactionHomeList = homeUiState.transactionHomeList,
            onItemClick = { navigateToItemUpdate(it.transactionId) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun TransactionsList(
    transactionHomeList: List<TransactionHome>,
    onItemClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(
            items = transactionHomeList
        ) { currentIndex, transactionHome ->
            TransactionsItem(
                transactionHome = transactionHome,
                index = currentIndex,
                onItemClick = onItemClick
            )
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun TransactionsItem(
    transactionHome: TransactionHome,
    index: Int,
    onItemClick: (Transaction) -> Unit,
) {
    val context = LocalContext.current
    Box(
        //add padding before setting size -> margin
        //add padding after setting size -> padding
        modifier = Modifier
            .background(color = if (index % 2 == 0) Color.White else Color(211, 211, 211))
            .padding(8.dp)
            .clickable { onItemClick(transactionHome.transaction) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (transactionHome.categoryIconName != "") {
                Icon(
                    painter = painterResource(
                        id = context.resources.getIdentifier(
                            transactionHome.categoryIconName,
                            "drawable",
                            context.packageName
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .weight(0.2f),
                    tint = Color.Unspecified
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                transactionHome.transaction.let {
                    Text(text = it.transactionName ?: "Chưa đề cập")
                    Text(text = it.transactionDate)
                }
            }
        }

        Text(
            text = transactionHome.transaction.transactionAmount.toString().groupAmount() + "đ",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoneyManagementTheme {
        //3 preview items
        TransactionsList(
            transactionHomeList = emptyList(),
            onItemClick = {}
        )
    }
}