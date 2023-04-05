package com.example.moneymanagement.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.database.entity.Transaction
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
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
    navigateToItemDetail: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val searchText by viewModel.searchText.collectAsState()
    val transactionList by viewModel.transactionList.collectAsState()

    HomeBody(
        navigateToItemEntry = navigateToItemEntry,
        navigateToItemDetail = navigateToItemDetail,
        transactionList = transactionList,
        searchText = searchText,
        onSearchFieldChange = viewModel::onSearchFieldChange
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeBody(
    navigateToItemEntry: () -> Unit,
    navigateToItemDetail: (Int) -> Unit,
    transactionList: List<TransactionWithCateAndSubcategory>,
    searchText: String,
    onSearchFieldChange: (String) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(imageVector = Icons.Filled.Add,null)
            }
        }
    ) {
        Column {
            var isFocused by remember { mutableStateOf(false) }
            val focusManager = LocalFocusManager.current
            TextField(
                value = searchText,
                onValueChange = onSearchFieldChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Tìm tên lịch sử giao dịch") },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        null,
                        modifier = Modifier
                            .clickable { onSearchFieldChange("") }
                            .onFocusChanged { isFocused = !isFocused }
                    )
                },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )

            Text("Filter")
//            Row {
//                transactionList.forEach { transaction ->
//                    Chip(onClick = { /*TODO*/ }) {
//                        Text(transaction.category.categoryName)
//                    }
//                }
//            }
            Spacer(modifier = Modifier.height(8.dp))
            TransactionsList(
                transactionHomeList = transactionList,
                onItemClick = { navigateToItemDetail(it.transactionId) },
            )
        }
    }
}

@Composable
fun TransactionsList(
    transactionHomeList: List<TransactionWithCateAndSubcategory>,
    onItemClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(
            items = transactionHomeList
        ) { currentIndex, transactionWithCateAndSubcategory ->
            TransactionsItem(
                transactionWithCateAndSubcategory = transactionWithCateAndSubcategory,
                index = currentIndex,
                onItemClick = onItemClick
            )
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun TransactionsItem(
    transactionWithCateAndSubcategory: TransactionWithCateAndSubcategory,
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
            .clickable { onItemClick(transactionWithCateAndSubcategory.transaction) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (transactionWithCateAndSubcategory.category.categoryIconName != "") {
                Icon(
                    painter = painterResource(
                        id = context.resources.getIdentifier(
                            transactionWithCateAndSubcategory.category.categoryIconName,
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
                transactionWithCateAndSubcategory.transaction.let {
                    Text(text = it.transactionName ?: "Chưa đề cập")
                    Text(text = it.transactionDate)
                }
            }
        }

        Text(
            text = transactionWithCateAndSubcategory.transaction.transactionAmount.toString().groupAmount() + "đ",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoneyManagementTheme {
        HomeBody(
            navigateToItemEntry = {},
            navigateToItemDetail = {},
            transactionList = listOf(
                TransactionWithCateAndSubcategory(),
                TransactionWithCateAndSubcategory(),
                TransactionWithCateAndSubcategory(),
                TransactionWithCateAndSubcategory(),
                TransactionWithCateAndSubcategory(),
            ),
            "",{}
        )
    }
}