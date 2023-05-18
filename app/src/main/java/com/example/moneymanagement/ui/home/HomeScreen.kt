package com.example.moneymanagement.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanagement.AppViewModelProvider
import com.example.moneymanagement.database.entity.Subcategory
import com.example.moneymanagement.database.entity.Transaction
import com.example.moneymanagement.database.model.CategoryWithSubcategories
import com.example.moneymanagement.database.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.ui.BottomNavigator
import com.example.moneymanagement.ui.detail.groupAmount
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.MoneyManagementTheme
import java.text.SimpleDateFormat
import java.util.*

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Home Screen"
}

@Composable
fun HomeScreen(
    navigateToStatScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemDetail: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val searchText by viewModel.searchText.collectAsState()
    val transactionList by viewModel.transactionList.collectAsState()
    val categoryWithSubCategoryList by viewModel.categoryWithSubCategoryList.collectAsState()
    val isCategoryChipSelected by viewModel.isCategoryChipSelected.collectAsState()
    val selectedCategoryChipIndex by viewModel.selectedCategoryChipIndex.collectAsState()
    val subcategoryList by viewModel.subcategoryList.collectAsState()
    val isSubcategoryChipSelected by viewModel.isSubcategoryChipSelected.collectAsState()
    val selectedSubcategoryChipId by viewModel.selectedSubcategoryChipId.collectAsState()

    HomeBody(
        navigateToStatScreen = navigateToStatScreen,
        navigateToItemEntry = navigateToItemEntry,
        navigateToItemDetail = navigateToItemDetail,
        transactionList = transactionList,
        searchText = searchText,
        onSearchFieldChange = viewModel::onSearchFieldChange,
        categoryWithSubCategoryList = categoryWithSubCategoryList,
        selectedCategoryChipIndex = selectedCategoryChipIndex,
        isCategoryChipSelected = isCategoryChipSelected,
        onCategoryChipChange = viewModel::onCategoryChipChange,
        subcategoryList = subcategoryList,
        isSubcategoryChipSelected = isSubcategoryChipSelected,
        onSubcategoryChipChange = viewModel::onSubcategoryChipChange,
        selectedSubcategoryChipId = selectedSubcategoryChipId
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeBody(
    navigateToStatScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemDetail: (Int) -> Unit,
    transactionList: List<TransactionWithCateAndSubcategory>,
    searchText: String,
    onSearchFieldChange: (String) -> Unit,
    categoryWithSubCategoryList: List<CategoryWithSubcategories>,
    selectedCategoryChipIndex: Int,
    isCategoryChipSelected: Boolean,
    onCategoryChipChange: (Int) -> Unit,
    subcategoryList: List<Subcategory>,
    isSubcategoryChipSelected: Boolean,
    onSubcategoryChipChange: (Int) -> Unit,
    selectedSubcategoryChipId: Int
) {
    Scaffold(
        bottomBar = { BottomNavigator({}, navigateToStatScreen, HomeDestination.route) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToItemEntry() }) {
                Icon(Icons.Filled.Add, null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        Column {
            /*
            Thanh tìm kiếm lịch sử giao dịch theo tên và nút thống kê
             */
            SearchBar(
                searchText = searchText,
                onSearchFieldChange = onSearchFieldChange
            )
            /*
            Mục ô chọn để lọc lịch sử giao dịch
             */
            Text("Filter")
            CategoryChips(
                categoryWithSubCategoryList,
                selectedCategoryChipIndex,
                isCategoryChipSelected,
                onCategoryChipChange,
                subcategoryList,
                isSubcategoryChipSelected,
                onSubcategoryChipChange,
                selectedSubcategoryChipId
            )

            Spacer(modifier = Modifier.height(8.dp))

            /*
            Mục hiển thị danh sách lịch sử giao dịch
             */
            TransactionsList(
                transactionHomeList = transactionList.groupBy {
                    val transactionDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(it.transaction.transactionDate)
                    SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(transactionDate!!)
                },
                onItemClick = { navigateToItemDetail(it.transactionId) },
            )
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchFieldChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    TextField(
        value = searchText,
        onValueChange = onSearchFieldChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryChips(
    categoryWithSubCategoryList: List<CategoryWithSubcategories>,
    selectedCategoryChipIndex: Int,
    isCategoryChipSelected: Boolean,
    onCategoryChipChange: (Int) -> Unit,
    subcategoryList: List<Subcategory>,
    isSubcategoryChipSelected: Boolean,
    onSubcategoryChipChange: (Int) -> Unit,
    selectedSubcategoryChipId: Int
) {
    LazyRow {
        itemsIndexed(categoryWithSubCategoryList) { index, item ->
            Chip(
                onClick = {
                    if (
                        isCategoryChipSelected &&
                        selectedCategoryChipIndex == index
                    )
                        onCategoryChipChange(-1)
                    else
                        onCategoryChipChange(index)
                    onSubcategoryChipChange(-1)
                },
                colors = if (selectedCategoryChipIndex.inc() == item.category.categoryId)
                    ChipDefaults.chipColors(backgroundColor = Color(0xFF1DB954))
                else
                    ChipDefaults.chipColors() //default
            ) {
                Text(text = item.category.categoryName)
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

    if (selectedCategoryChipIndex == -1) return
    /*
    Hiển thị chip mở rộng nếu thể loại cha đã được chọn
     */
    LazyRow {
        items(subcategoryList) { item ->
            Chip(
                onClick = {
                    if (isSubcategoryChipSelected && selectedSubcategoryChipId == item.subcategoryId)
                        onSubcategoryChipChange(-1)
                    else
                        onSubcategoryChipChange(item.subcategoryId)
                },
                colors = if (selectedSubcategoryChipId == item.subcategoryId)
                    ChipDefaults.chipColors(backgroundColor = Color(0xFF1DB954))
                else
                    ChipDefaults.chipColors() //default
            ) {
                Text(text = item.subcategoryName)
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun TransactionsList(
    transactionHomeList: Map<String, List<TransactionWithCateAndSubcategory>>,
    onItemClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        transactionHomeList.forEach { (date, transactions) ->
            item {
                Text(
                    text = date.toFormattedMonth(),
                    fontSize = 23.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(212, 175, 55))
                        .padding(vertical = 4.dp)
                )
                Column(modifier = modifier.fillMaxWidth()) {
                    transactions.forEachIndexed { index, transaction ->
                        TransactionsItem(
                            transactionWithCateAndSubcategory = transaction,
                            index = index,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
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
            .background(color = if (index % 2 == 0) Color(18, 18, 18) else Color(31, 27, 36))
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
                    Text(text = it.transactionDate.toFormattedDate())
                }
            }
        }

        Text(
            text = transactionWithCateAndSubcategory.transaction.transactionAmount.toString()
                .groupAmount() + "đ",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

fun String.toFormattedMonth(): String {
    if (this.isEmpty()) return ""

    val inputFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("'Tháng' MM/yyyy", Locale("vi", "VN"))

    val date = inputFormat.parse(this)
    return outputFormat.format(date!!)
}

fun String.toFormattedDate(): String {
    if (this.isEmpty()) return ""

    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, 'ngày' dd/MM", Locale("vi", "VN"))

    val date = inputFormat.parse(this)
    return outputFormat.format(date!!)
}

@Composable
@Preview
fun PreviewTransactionList() {
    MoneyManagementTheme {
        TransactionsList(
            transactionHomeList = sampleData.groupBy { it.transaction.transactionDate },
            onItemClick = {}
        )
    }
}

val sampleData = listOf(
    TransactionWithCateAndSubcategory(
        transaction = Transaction(0, "", 1.0, "2002-10-14", "", 1, 1)
    ),
    TransactionWithCateAndSubcategory(
        transaction = Transaction(1, "", 1.0, "2002-10-14", "", 1, 1)
    ),
    TransactionWithCateAndSubcategory(
        transaction = Transaction(2, "", 1.0, "2002-10-13", "", 1, 1)
    ),
    TransactionWithCateAndSubcategory(
        transaction = Transaction(3, "", 1.0, "2002-10-12", "", 1, 1)
    ),
    TransactionWithCateAndSubcategory(
        transaction = Transaction(4, "", 1.0, "2002-10-12", "", 1, 1)
    ),
    TransactionWithCateAndSubcategory(
        transaction = Transaction(5, "", 1.0, "2002-10-11", "", 1, 1)
    ),
)