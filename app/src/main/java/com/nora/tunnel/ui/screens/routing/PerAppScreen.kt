package com.nora.tunnel.ui.screens.routing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class AppItem(val packageName: String, val name: String, val selected: Boolean)

@Composable
fun PerAppScreen() {
    var mode by remember { mutableStateOf("All applications") }
    val apps = remember { listOf(
        AppItem("com.android.browser", "Browser", false),
        AppItem("com.termux", "Termux", true)
    )}

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Application Routing", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Mode: $mode", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(apps) { app ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(app.name)
                    Checkbox(checked = app.selected, onCheckedChange = { })
                }
            }
        }
    }
}
