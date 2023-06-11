package com.example.moneymanagement.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import com.example.moneymanagement.data.entity.Category
import com.example.moneymanagement.data.entity.Subcategory
import com.example.moneymanagement.data.entity.Transaction
import com.example.moneymanagement.data.model.TransactionWithCateAndSubcategory
import com.example.moneymanagement.ui.BottomNavigator
import com.example.moneymanagement.ui.detail.groupAmount
import com.example.moneymanagement.ui.navigation.NavigationDestination
import com.example.moneymanagement.ui.theme.*
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

    val categoryList by viewModel.categoryList.collectAsState()
    val isCategoryChipSelected by viewModel.isCategoryChipSelected.collectAsState()
    val selectedCategoryChipId by viewModel.selectedCategoryChipId.collectAsState()
    val subcategoryList by viewModel.subcategoryList.collectAsState()
    val isSubcategoryChipSelected by viewModel.isSubcategoryChipSelected.collectAsState()
    val selectedSubcategoryChipId by viewModel.selectedSubcategoryChipId.collectAsState()

    val transactionsGroupByMonthOfYear by viewModel.transactionsGroupByMonthOfYear.collectAsState()

    HomeBody(
        navigateToStatScreen = navigateToStatScreen,
        navigateToItemEntry = navigateToItemEntry,
        navigateToItemDetail = navigateToItemDetail,

        searchText = searchText,
        onSearchFieldChange = viewModel::onSearchFieldChange,

        categoryList = categoryList,
        selectedCategoryChipId = selectedCategoryChipId,
        isCategoryChipSelected = isCategoryChipSelected,
        onCategoryChipChange = viewModel::onCategoryChipChange,
        subcategoryList = subcategoryList,
        isSubcategoryChipSelected = isSubcategoryChipSelected,
        onSubcategoryChipChange = viewModel::onSubcategoryChipChange,
        selectedSubcategoryChipId = selectedSubcategoryChipId,

        transactionsGroupByMonthOfYear = transactionsGroupByMonthOfYear
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeBody(
    navigateToStatScreen: () -> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemDetail: (Int) -> Unit,

    searchText: String,
    onSearchFieldChange: (String) -> Unit,

    categoryList: List<Category>,
    selectedCategoryChipId: Int,
    isCategoryChipSelected: Boolean,
    onCategoryChipChange: (Int) -> Unit,
    subcategoryList: List<Subcategory>,
    isSubcategoryChipSelected: Boolean,
    onSubcategoryChipChange: (Int) -> Unit,
    selectedSubcategoryChipId: Int,

    transactionsGroupByMonthOfYear: Map<String, List<TransactionWithCateAndSubcategory>>
) {
    Scaffold(
        bottomBar = {
            BottomNavigator(
                {},
                navigateToStatScreen,
                navigateToItemEntry,
                HomeDestination.route
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
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
                categoryList,
                selectedCategoryChipId,
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
                transactionsGroupByMonthOfYear = transactionsGroupByMonthOfYear,
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
    categoryList: List<Category>,
    selectedCategoryChipId: Int,
    isCategoryChipSelected: Boolean,
    onCategoryChipChange: (Int) -> Unit,
    subcategoryList: List<Subcategory>,
    isSubcategoryChipSelected: Boolean,
    onSubcategoryChipChange: (Int) -> Unit,
    selectedSubcategoryChipId: Int
) {
    LazyRow {
        items(categoryList) { item ->
            val categoryId = item.categoryId
            Chip(
                onClick = {
                    if (
                        isCategoryChipSelected &&
                        selectedCategoryChipId == categoryId
                    )
                        onCategoryChipChange(-1)
                    else
                        onCategoryChipChange(categoryId)
                    onSubcategoryChipChange(-1)
                },
                colors = if (selectedCategoryChipId == item.categoryId)
                    ChipDefaults.chipColors(backgroundColor = limeGreen)
                else
                    ChipDefaults.chipColors() //default
            ) {
                Text(text = item.categoryName)
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

    if (selectedCategoryChipId == -1) return
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
                    ChipDefaults.chipColors(backgroundColor = limeGreen)
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
    transactionsGroupByMonthOfYear: Map<String, List<TransactionWithCateAndSubcategory>>,
    onItemClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        transactionsGroupByMonthOfYear.forEach { (_, transactions) ->
            item {
                Text(
                    text = transactions.first().transaction.transactionDate.toDisplayMonth(),
                    fontSize = 23.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(goldenrod)
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
            .background(color = if (index % 2 == 0) jetBlack else darkCharcoal)
            .padding(8.dp)
            .clickable { onItemClick(transactionWithCateAndSubcategory.transaction) },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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

            Column(modifier = Modifier.weight(1f)) {
                transactionWithCateAndSubcategory.transaction.let {
                    Text(text = it.transactionName ?: "Chưa đề cập")
                    Text(text = it.transactionDate.toDisplayDate())
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

fun String.toDisplayMonth(): String {
    val date = STRING_DATE_FORMATTER.parse(this)
    return DISPLAY_MONTH_FORMATTER.format(date!!)
}

fun String.toDisplayDate(): String {
    if (this.isEmpty()) return ""
    val date = STRING_DATE_FORMATTER.parse(this)
    return DISPLAY_DATE_FORMATTER.format(date!!)
}

@Composable
@Preview
fun PreviewTransactionList() {
    MoneyManagementTheme(darkTheme = true) {
        TransactionsList(
            transactionsGroupByMonthOfYear = sampleData,
            onItemClick = {}
        )
    }
}

val sampleData = mapOf(
    Pair(
        first = "2023-06",
        second = listOf(
            TransactionWithCateAndSubcategory(
                Transaction(0, "Nhậu", 200000.0, "2023-06-03", null, 1, 1),
                Category(1,"Ăn","category_icon_1"),
                Subcategory(1,"ăn",1)
            ),
            TransactionWithCateAndSubcategory(
                Transaction(0, "nhẹt", 200000.0, "2023-06-05", null, 1, 1),
                Category(1,"Ăn","category_icon_1"),
                Subcategory(1,"ăn",1)
            )
        )
    )
)

val STRING_DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd", Locale.US)
val DISPLAY_MONTH_FORMATTER = SimpleDateFormat("'Tháng' MM/yyyy", Locale("vi", "VN"))
val DISPLAY_DATE_FORMATTER = SimpleDateFormat("EEEE, 'ngày' dd/MM", Locale("vi", "VN"))