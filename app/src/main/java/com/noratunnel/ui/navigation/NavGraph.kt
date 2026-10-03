package com.noratunnel.ui.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noratunnel.MainActivity
import com.noratunnel.ui.screens.*
@Composable
fun NoraNavGraph(activity: MainActivity) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav, activity) }
        composable("servers") { ServersScreen(nav) }
        composable("add_server") { AddEditServerScreen(nav, null) }
        composable("edit_server/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            AddEditServerScreen(nav, id)
        }
        composable("import") { ImportConfigScreen(nav) }
        composable("qr") { QrScannerScreen(nav) }
        composable("details") { ConnectionDetailsScreen(nav) }
        composable("stats") { StatisticsScreen(nav) }
        composable("logs") { LogsScreen(nav) }
        composable("settings") { SettingsScreen(nav) }
        composable("security") { SecurityScreen(nav) }
        composable("about") { AboutScreen(nav) }
    }
}
