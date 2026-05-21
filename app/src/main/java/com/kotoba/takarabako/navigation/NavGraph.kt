package com.kotoba.takarabako.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kotoba.takarabako.ui.screens.FavoritesScreen
import com.kotoba.takarabako.ui.screens.HomeScreen
import com.kotoba.takarabako.ui.screens.JlptScreen
import com.kotoba.takarabako.ui.screens.QuoteCardScreen
import com.kotoba.takarabako.ui.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable(
            route = "quote/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            QuoteCardScreen(
                navController = navController,
                category = backStackEntry.arguments?.getString("category") ?: "전체"
            )
        }
        composable(
            route = "jlpt/{level}",
            arguments = listOf(navArgument("level") { type = NavType.StringType })
        ) { backStackEntry ->
            JlptScreen(
                navController = navController,
                level = backStackEntry.arguments?.getString("level") ?: "all"
            )
        }
        composable("favorites") {
            FavoritesScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
    }
}
