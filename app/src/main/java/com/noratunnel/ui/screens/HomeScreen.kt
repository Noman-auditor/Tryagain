package com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noratunnel.MainActivity
import com.noratunnel.domain.model.VpnState
import com.noratunnel.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(nav: NavController, activity: MainActivity, vm: HomeViewModel = hiltViewModel()) {
    val status by vm.vpnStatus.collectAsState()
    val servers by vm.servers.collectAsState()
    val selectedId by vm.selectedId.collectAsState()
    val selected = servers.find { it.id == selectedId } ?: servers.firstOrNull()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("NORA TUNNEL", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Secure. Private. Connected.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(24.dp))
        
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                val color = when(status.state){
                    VpnState.CONNECTED -> Color(0xFF00E676)
                    VpnState.CONNECTING, VpnState.RECONNECTING -> Color(0xFFFFC400)
                    VpnState.ERROR -> Color(0xFFFF5252)
                    else -> Color.Gray
                }
                Box(Modifier.size(110.dp), contentAlignment = Alignment.Center){
                    Card(shape = RoundedCornerShape(50.dp), colors = CardDefaults.cardColors(containerColor = color), modifier = Modifier.size(90.dp)){}
                    Text(status.state.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text(status.serverName ?: selected?.name ?: "No Server Selected", fontWeight = FontWeight.SemiBold)
                if(status.state == VpnState.CONNECTED){
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
                        Column(horizontalAlignment = Alignment.CenterHorizontally){ Text("${status.latencyMs ?: 0} ms", fontWeight = FontWeight.Bold); Text("Latency", style = MaterialTheme.typography.labelSmall) }
                        Column(horizontalAlignment = Alignment.CenterHorizontally){ Text("${status.durationSeconds/60}m ${status.durationSeconds%60}s", fontWeight = FontWeight.Bold); Text("Duration", style = MaterialTheme.typography.labelSmall) }
                        Column(horizontalAlignment = Alignment.CenterHorizontally){ Text(status.health, fontWeight = FontWeight.Bold); Text("Health", style = MaterialTheme.typography.labelSmall) }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("VPN IP: ${status.vpnIp ?: selected?.clientAddress ?: "Unavailable"}", style = MaterialTheme.typography.labelSmall)
                }
                status.error?.let{ Text(it, color = Color.Red, modifier = Modifier.padding(top=8.dp)) }
            }
        }
        Spacer(Modifier.height(20.dp))
        if(status.state == VpnState.CONNECTED || status.state == VpnState.CONNECTING || status.state == VpnState.RECONNECTING){
            Button(onClick = { vm.disconnect(activity) }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3D00))) {
                Text("DISCONNECT", fontWeight = FontWeight.Bold)
            }
        } else {
            Button(enabled = selected != null, onClick = { selected?.let{ vm.connect(vm.getProfileEntityToModel(it), activity)} }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("CONNECT", fontWeight = FontWeight.Bold)
            }
            if(selected==null) Text("Add a server to connect", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top=8.dp))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            OutlinedButton(onClick = { nav.navigate("servers") }){ Text("Servers") }
            OutlinedButton(onClick = { nav.navigate("details") }){ Text("Details") }
            OutlinedButton(onClick = { nav.navigate("settings") }){ Text("Settings") }
        }
        TextButton(onClick = { nav.navigate("logs") }){ Text("View Logs • Diagnostics") }
    }
}
