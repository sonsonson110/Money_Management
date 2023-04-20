package com.example.moneymanagement.ui.edit

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.ui.entry.TransactionEntryBody
import com.example.moneymanagement.ui.entry.TransactionEntryDestination
import com.example.moneymanagement.ui.navigation.NavigationDestination
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
        val categoryList by viewModel.categoryList.collectAsState()
        TransactionEntryBody(
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTransaction()
                    navigateBack()
                }
            },
            transactionEntry = viewModel.transactionEditUiState.transactionEntry,
            categoryList = categoryList,
            buttonFulfilled = viewModel.transactionEditUiState.isEntryValid,
            onValueChange = viewModel::updateUiState,
            currentSelectedCategoryId = viewModel.transactionEditUiState.currentSelectedCategoryId
        )
    }
}