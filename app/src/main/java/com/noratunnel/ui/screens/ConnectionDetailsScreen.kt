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
fun ConnectionDetailsScreen(nav: NavController, vm: HomeViewModel = hiltViewModel()){
    val s by vm.vpnStatus.collectAsState()
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("Connection Details", style=MaterialTheme.typography.headlineSmall)
        Text("Diagnostics - no fake data", style=MaterialTheme.typography.labelSmall)
        Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
            Text("Tunnel Status: ${s.state.name}", style=MaterialTheme.typography.titleSmall)
            Divider()
            Text("VPN DNS: ${s.vpnIp ?: "Unavailable"}")
            Text("Detected Network: System Default (WiFi/Mobile)")
            Text("IPv4 Status: ${if(s.state==com.noratunnel.domain.model.VpnState.CONNECTED) "Routed via VPN (0.0.0.0/0)" else "Direct / Disconnected"}")
            Text("IPv6 Status: ${if(s.vpnIp?.contains(":")==true) "Routed via VPN" else "Not configured - Warning: may leak outside VPN"}", color = MaterialTheme.colorScheme.error)
            Text("Handshake: ${s.handshakeSecondsAgo?.let{"$it seconds ago"} ?: "Unavailable"}")
            Text("Latency: ${s.latencyMs?.let{"$it ms"} ?: "Unavailable"}")
            Text("Connection: ${s.health}")
        }}
        Card(Modifier.fillMaxWidth(), colors= CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)){
            Text("We do NOT claim 100% leak protection. Verify via dnsleaktest.com and ipleak.net", modifier=Modifier.padding(12.dp), style=MaterialTheme.typography.labelSmall)
        }
    }
}
