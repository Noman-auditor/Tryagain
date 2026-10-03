package com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noratunnel.ui.viewmodel.HomeViewModel

@Composable
fun LogsScreen(nav: NavController, vm: HomeViewModel = hiltViewModel()){
    val logs by vm.logs.collectAsState()
    Column(Modifier.padding(16.dp).fillMaxSize()){
        Text("Diagnostics Logs", style=MaterialTheme.typography.headlineSmall)
        Text("Safe diagnostics - NEVER logs PrivateKey/Tokens", style=MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Card(Modifier.fillMaxWidth().padding(vertical=8.dp)){ Column(Modifier.padding(8.dp)){
            Text("Example:", style=MaterialTheme.typography.labelSmall)
            Text("22:01 VPN permission granted\n22:02 Connecting\n22:03 Handshake established\n22:03 Tunnel connected\n22:10 Network changed -> Reconnecting", style=MaterialTheme.typography.bodySmall)
        }}
        Divider()
        LazyColumn(modifier = Modifier.weight(1f)){ items(logs){ Text(it, style=MaterialTheme.typography.bodySmall) } }
        if(logs.isEmpty()) Text("No logs yet", style=MaterialTheme.typography.bodySmall)
    }
}
