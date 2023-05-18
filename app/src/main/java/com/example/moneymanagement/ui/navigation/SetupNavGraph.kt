package com.example.moneymanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moneymanagement.ui.chart.StatDestination
import com.example.moneymanagement.ui.chart.StatScreen
import com.example.moneymanagement.ui.detail.TransactionDetailDestination
import com.example.moneymanagement.ui.detail.TransactionDetailScreen
import com.example.moneymanagement.ui.edit.TransactionEditDestination
import com.example.moneymanagement.ui.edit.TransactionEditScreen
import com.example.moneymanagement.ui.entry.TransactionEntryDestination
import com.example.moneymanagement.ui.entry.TransactionEntryScreen
import com.example.moneymanagement.ui.home.HomeDestination
import com.example.moneymanagement.ui.home.HomeScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(TransactionEntryDestination.route) },
                navigateToItemDetail = { navController.navigate("${TransactionDetailDestination.route}/$it") },
                navigateToStatScreen = { navController.navigate(StatDestination.route) }
            )
        }

        //detail screen route
        composable(
            route = TransactionDetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(TransactionDetailDestination.transactionIdArg) {
                    type = NavType.IntType
                }
            )
        )
        {
            TransactionDetailScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToTransactionUpdate = {
                    navController.navigate(route = "${TransactionEditDestination.route}/$it")
                }
            )
        }

        //Entry route
        composable(route = TransactionEntryDestination.route) {
            TransactionEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        //Edit route
        composable(
            route = TransactionEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(TransactionEditDestination.transactionIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            TransactionEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }

        //Stat route
        composable(route = StatDestination.route) {
            StatScreen(
                navigateToHomeScreen = { navController.navigate(HomeDestination.route) },
                navigateToItemEntry = { navController.navigate(TransactionEntryDestination.route) },
                navigateToItemDetail = { navController.navigate("${TransactionDetailDestination.route}/$it") }
            )
        }
    }
}