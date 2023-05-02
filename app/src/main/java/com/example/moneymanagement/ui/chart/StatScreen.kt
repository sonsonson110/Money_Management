package com.example.moneymanagement.ui.chart

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import com.example.moneymanagement.ui.BottomNavigator
import com.example.moneymanagement.ui.navigation.NavigationDestination

object StatDestination : NavigationDestination {
    override val route = "stat_destination"
    override val title = "stat screen"
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToItemEntry: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        bottomBar = { BottomNavigator(navigateToHomeScreen, {}, StatDestination.route)},
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToItemEntry() }) {
                Icon(Icons.Filled.Add, null)
            }
        },
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        Text(text = "hi")
    }
}