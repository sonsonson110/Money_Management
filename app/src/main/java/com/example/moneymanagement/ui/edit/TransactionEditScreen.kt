package com.example.moneymanagement.ui.edit

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.moneymanagement.ui.detail.TransactionDetailDestination
import com.example.moneymanagement.ui.entry.TransactionEntryBody
import com.example.moneymanagement.ui.entry.TransactionEntryDestination
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme

object TransactionEditDestination : NavigationDestination {
    override val route = "transaction_edit"
    override val title = "Transaction Edit Screen"
    const val transactionIdArg = "transactionId"
    val routeWithArgs = "${TransactionDetailDestination.route}/{$transactionIdArg}"
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TransactionEditScreen(
    //TODO: navigateBack, onNavigateUp, viewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {/*TODO*/}) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = TransactionEntryDestination.title) },
            )
        }
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun Test() {
    MoneyManagementTheme {
        TransactionEditScreen()
    }
}