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
fun ServersScreen(nav: NavController, vm: HomeViewModel = hiltViewModel()){
    val servers by vm.servers.collectAsState()
    Scaffold(floatingActionButton = { FloatingActionButton(onClick = { nav.navigate("add_server")}){ Text("+") } }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)){
            Text("Servers", style = MaterialTheme.typography.headlineSmall)
            Text("User-owned VPS only. Examples: 🇸🇬 Singapore, 🇯🇵 Japan, 🇩🇪 Germany, 🇳🇱 Netherlands, 🇺🇸 United States. You must provide and control actual servers.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            LazyColumn(modifier = Modifier.weight(1f)){
                items(servers){ s ->
                    Card(Modifier.fillMaxWidth().padding(vertical=6.dp), onClick = { nav.navigate("edit_server/${s.id}") }){
                        Column(Modifier.padding(16.dp)){
                            Text(s.name, style = MaterialTheme.typography.titleMedium)
                            Text(s.endpoint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            Text("AllowedIPs: ${s.allowedIps}", style = MaterialTheme.typography.labelSmall)
                            Text("DNS: ${s.dns}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                Button(onClick = { nav.navigate("import")}){ Text("Import .conf") }
                Button(onClick = { nav.navigate("qr")}){ Text("Scan QR") }
            }
        }
    }
}
