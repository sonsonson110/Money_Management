package com.example.moneymanagement.ui.edit

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.ui.entry.TransactionEntryBody
import com.example.moneymanagement.ui.entry.TransactionEntryDestination
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme
import kotlinx.coroutines.launch

object TransactionEditDestination : NavigationDestination {
    override val route = "transaction_edit"
    override val title = "Transaction Edit Screen"
    const val transactionIdArg = "transactionId"
    val routeWithArgs = "${route}/{$transactionIdArg}"
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TransactionEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: TransactionEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = TransactionEntryDestination.title) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        val categoryState by viewModel.categoryState.collectAsState()
        TransactionEntryBody(
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTransaction()
                    navigateBack()
                }
            },
            transactionEntry = viewModel.transactionEditUiState.transactionEntry,
            categoryList = categoryState.categoryWithSubcategoriesList,
            buttonFulfilled = viewModel.transactionEditUiState.isEntryValid,
            onValueChange = viewModel::updateUiState
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Test() {
    MoneyManagementTheme {
    }
}