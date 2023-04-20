package com.example.moneymanagement.ui.entry

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.database.model.CategoryWithSubcategories
import com.example.moneymanagement.database.entity.Subcategory
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme
import kotlinx.coroutines.launch

object TransactionEntryDestination : NavigationDestination {
    override val route = "transaction_entry"
    override val title = "Transaction Entry Screen"
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TransactionEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: TransactionEntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = TransactionEntryDestination.title) },
            )
        }
    ) {
        val coroutineScope = rememberCoroutineScope()
        val categoryList by viewModel.categoryList.collectAsState()
        TransactionEntryBody(
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTransaction()
                    navigateBack()
                }
            },
            transactionEntry = viewModel.transactionEntryUiState.transactionEntry,
            categoryList = categoryList,
            buttonFulfilled = viewModel.transactionEntryUiState.isEntryValid,
            onValueChange = viewModel::updateUiState,
            currentSelectedCategoryId = viewModel.transactionEntryUiState.currentSelectedCategoryId
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TransactionEntryBody(
    onSaveClick: () -> Unit,
    transactionEntry: TransactionEntry,
    categoryList: List<CategoryWithSubcategories>,
    buttonFulfilled: Boolean,
    onValueChange: (TransactionEntry) -> Unit,
    currentSelectedCategoryId: Int
) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        transactionEntry.let {
            Text(
                text = "Tên giao dịch",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = it.transactionName,
                onValueChange = { name -> onValueChange(transactionEntry.copy(transactionName = name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Text(
                text = "Ngày giao dịch*",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = it.transactionDate,
                onValueChange = { date -> onValueChange(transactionEntry.copy(transactionDate = date)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Text(
                text = "Số tiền*",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = it.transactionAmount.ifBlank { "" },
                onValueChange = { amount ->
                    onValueChange(
                        transactionEntry.copy(
                            transactionAmount = amount
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Text(
                text = "Ghi chú",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = it.transactionNote,
                onValueChange = { note -> onValueChange(transactionEntry.copy(transactionNote = note)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }

        Text(
            text = "Phân loại giao dịch*",
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        var subcategoriesFromCategory by remember { mutableStateOf(emptyList<Subcategory>()) }

        CategorySelectionRow(
            list = categoryList,
            transactionEntry = transactionEntry,
            onCategorySelect = onValueChange,
            updateSubcategories = { newList -> subcategoriesFromCategory = newList },
            currentSelectedCategoryId = currentSelectedCategoryId
        )

        SubcategoryDropdownMenu(
            list = subcategoriesFromCategory,
            transactionEntry = transactionEntry,
            onSubcategorySelect = onValueChange,
        )

        //for testing
        Text(text = "testing")
        Text(text = transactionEntry.category.categoryId.toString())
        transactionEntry.subcategory?.let { Text(text = it.subcategoryId.toString()) }

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = buttonFulfilled,
        ) { Text(text = "Save") }
    }

}

@SuppressLint("DiscouragedApi")
@Composable
fun CategorySelectionRow(
    list: List<CategoryWithSubcategories>,
    transactionEntry: TransactionEntry,
    updateSubcategories: (List<Subcategory>) -> Unit,
    onCategorySelect: (TransactionEntry) -> Unit,
    currentSelectedCategoryId: Int
) {
    val context = LocalContext.current

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(list) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .size(width = 80.dp, height = 100.dp)
                    .border(
                        BorderStroke(
                            width = 2.dp,
                            color = if (currentSelectedCategoryId == item.category.categoryId) Color.Red else Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = if (currentSelectedCategoryId == item.category.categoryId) Color.Gray else Color.Transparent)
                    .padding(8.dp)
                    .selectable(
                        selected = currentSelectedCategoryId == item.category.categoryId,
                        onClick = {
                            onCategorySelect(
                                transactionEntry.copy(
                                    category = item.category,
                                    subcategory = null,
                                )
                            )
                            updateSubcategories(item.subcategories)
                        }
                    )
            ) {
                Icon(
                    painter = painterResource(
                        id = context.resources.getIdentifier(
                            item.category.categoryIconName,
                            "drawable",
                            context.packageName
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .weight(0.6f),
                    tint = Color.Unspecified
                )
                Text(
                    text = item.category.categoryName,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(0.4f)
                )
            }
        }
    }
}

@Composable
fun SubcategoryDropdownMenu(
    list: List<Subcategory>,
    transactionEntry: TransactionEntry,
    onSubcategorySelect: (TransactionEntry) -> Unit,
) {
    var expansionState by remember { mutableStateOf(false) }
    val icon = if (expansionState) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    TextField(
        value = if (transactionEntry.subcategory != null) transactionEntry.subcategory.subcategoryName else "",
        onValueChange = {},
        label = { Text(text = "Mở rộng") },
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.clickable { expansionState = !expansionState })
        },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true
    )
    DropdownMenu(
        expanded = expansionState,
        onDismissRequest = { expansionState = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        list.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    expansionState = false
                    onSubcategorySelect(transactionEntry.copy(subcategory = item))
                }
            ) {
                Text(item.subcategoryName)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MoneyManagementTheme {

    }
}



