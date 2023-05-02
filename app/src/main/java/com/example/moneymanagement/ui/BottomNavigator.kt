package com.example.moneymanagement.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import com.example.moneymanagement.ui.chart.StatDestination
import com.example.moneymanagement.ui.home.HomeDestination

@Composable
fun BottomNavigator(
    navigateToHomeScreen: () -> Unit,
    navigateToStatScreen: () -> Unit,
    defaultScreen: String
) {

    var selectedItem by remember { mutableStateOf(defaultScreen) }

    BottomAppBar(
        cutoutShape = CircleShape
    ) {
        //Home
        BottomNavigationItem(
            selected = selectedItem == HomeDestination.route,
            onClick = {
                selectedItem = HomeDestination.route
                navigateToHomeScreen()
            },
            icon = { Icon(Icons.Filled.Home, null) }
        )
        //Empty
        BottomNavigationItem(
            selected = false,
            onClick = {},
            icon = {},
            enabled = false
        )
        //Stat
        BottomNavigationItem(
            selected = selectedItem == StatDestination.route,
            onClick = {
                selectedItem = StatDestination.route
                navigateToStatScreen()
            },
            icon = { Icon(Icons.Filled.Menu, null) }
        )
    }
}
