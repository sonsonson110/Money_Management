package com.example.moneymanagement.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object TransactionDetailDestination : NavigationDestination {
    override val route = "transaction_detail"
    override val title = "Transaction Detail Screen"
    const val transactionIdArg = "transactionId"
    val routeWithArgs = "$route/{$transactionIdArg}"
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TransactionDetailScreen(
    onNavigateUp: () -> Unit,
    viewModel: TransactionDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = Color(230, 230, 230),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = TransactionDetailDestination.title) }
            )
        }

    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DetailCards(transactionWithCateAndSubcategory = uiState.transactionWithCateAndSubcategory)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = {
                        //TODO Implement update !
                    },
                    modifier = Modifier.weight(0.5f)
                ) { Text(text = "Edit") }

                OutlinedButton(onClick = {
                    coroutineScope.launch {
                        viewModel.deleteTransaction()
                        onNavigateUp()
                    }
                }, modifier = Modifier.weight(0.5f)) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@Composable
fun DetailCards(transactionWithCateAndSubcategory: TransactionWithCateAndSubcategory) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DetailCard {
            //Big header with transaction amount
            transactionWithCateAndSubcategory.let {
                BigDetailHeader(
                    transactionName = it.transaction.transactionName ?: "Chưa đề cập",
                    transactionAmount = it.transaction.transactionAmount,
                    iconName = it.category.categoryIconName
                )

                //Smaller transaction details
                DetailRow(title = "Thời gian", content = it.transaction.transactionDate)
                it.transaction.transactionNote?.let { date ->
                    DetailRow(
                        title = "Ghi chú",
                        content = date
                    )
                }
            }
        }

        DetailCard {
            transactionWithCateAndSubcategory.let {
                DetailRow(title = "Thể loại", content = it.category.categoryName)
                DetailRow(title = "Thể loại con", content = it.subcategory?.subcategoryName ?: "Chưa đề cập")
            }
        }
    }
}

@Composable
fun DetailCard(content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) { content() }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun BigDetailHeader(
    transactionName: String,
    transactionAmount: Double,
    iconName: String,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        // something not optimize here
        if (iconName != "") {
            Icon(
                painter = painterResource(
                    id = context.resources.getIdentifier(
                        iconName,
                        "drawable",
                        context.packageName
                    )
                ),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.2f)
                    .size(58.dp),
                tint = Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = transactionName,
                fontSize = 18.sp
            )

            Text(
                text = transactionAmount.toString().groupAmount() + "đ",
                fontSize = 26.sp
            )
        }
    }
}

@Composable
fun DetailRow(
    title: String,
    content: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, modifier = Modifier.weight(0.5f), textAlign = TextAlign.Left)
        Text(
            text = content,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.Right
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MoneyManagementTheme {
        DetailCards(
            transactionWithCateAndSubcategory = TransactionWithCateAndSubcategory()
        )
    }
}

fun String.groupAmount(): String {
    val customSeparator = DecimalFormatSymbols()
    customSeparator.apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val df = DecimalFormat("#,###.##", customSeparator)
    return df.format(this.toDouble())
}