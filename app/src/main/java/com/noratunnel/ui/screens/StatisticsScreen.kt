package com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noratunnel.ui.viewmodel.HomeViewModel

@Composable
fun StatisticsScreen(nav: NavController, vm: HomeViewModel = hiltViewModel()){
    val s by vm.vpnStatus.collectAsState()
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("Statistics & Health", style=MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)){
            Text("Latency: ${s.latencyMs ?: "Unavailable"} ms", style=MaterialTheme.typography.titleMedium)
            Text("Handshake: ${s.handshakeSecondsAgo?.let{"$it sec ago"} ?: "Unavailable"}")
            Text("Health: ${s.health}")
            Divider(Modifier.padding(vertical=6.dp))
            Text("Upload: ${s.uploadBytes} bytes")
            Text("Download: ${s.downloadBytes} bytes")
            Text("Server Reachable: ${if(s.state==com.noratunnel.domain.model.VpnState.CONNECTED) "Yes" else "No"}")
            Text("CPU / RAM / Disk / Uptime: Unavailable (enable optional backend GET /server/status)")
        }}
        Text("No fake statistics. If backend unavailable, shows Unavailable.", style=MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
    }
}
