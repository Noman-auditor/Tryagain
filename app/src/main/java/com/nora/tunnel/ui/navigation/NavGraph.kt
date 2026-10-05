package com.nora.tunnel.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nora.tunnel.ui.screens.home.HomeScreen
import com.nora.tunnel.ui.screens.routing.PerAppScreen

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Rounded.Home, "Home")
    object Profiles : Screen("profiles", Icons.Rounded.List, "Profiles")
    object Routing : Screen("routing", Icons.Rounded.Route, "Routing")
    object Lab : Screen("lab", Icons.Rounded.Science, "Lab")
    object Stats : Screen("stats", Icons.Rounded.QueryStats, "Stats")
    object Logs : Screen("logs", Icons.Rounded.Article, "Logs")
    object Security : Screen("security", Icons.Rounded.Security, "Security")
    object Settings : Screen("settings", Icons.Rounded.Settings, "Settings")
}

@Composable
fun NoraNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Routing.route) { PerAppScreen() }
        // Add remaining destinations as screens are fully implemented
    }
}
