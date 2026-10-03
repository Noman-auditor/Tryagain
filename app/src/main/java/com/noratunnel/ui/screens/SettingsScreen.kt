package com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(nav: NavController){
    var auto by remember{ mutableStateOf(false)}
    var reconnect by remember{ mutableStateOf(true)}
    var kill by remember{ mutableStateOf(false)}
    var dns by remember{ mutableStateOf("vpn")}
    var ipv6 by remember{ mutableStateOf("auto")}
    var split by remember{ mutableStateOf(false)}
    Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("Settings", style=MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){ Text("Auto Connect"); Switch(auto,{auto=it}) }
            Text("Automatically connect to last server on app start.", style=MaterialTheme.typography.labelSmall)
            Divider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){ Text("Reconnect Automatically"); Switch(reconnect,{reconnect=it}) }
            Text("Uses exponential backoff (1s -> 60s) to avoid loops on network change.", style=MaterialTheme.typography.labelSmall)
            Divider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){ Text("Kill Switch (Lockdown)"); Switch(kill,{kill=it}) }
            Text("Uses Android VPN lockdown (Always-on + Block connections without VPN). VPN active -> traffic via VPN. VPN disconnected + lockdown enabled -> block traffic. Enable in System Settings > VPN > Gear icon. Android 8+ required, OEM behavior varies.", style=MaterialTheme.typography.labelSmall)
            Divider()
            Text("DNS Mode: $dns"); Row{ listOf("vpn","custom","system").forEach{ Button(onClick={dns=it}, modifier=Modifier.padding(4.dp)){ Text(it) } } }
            Text("VPN DNS routes via tunnel. System may leak - we do not claim 100% protection. See Diagnostics.", style=MaterialTheme.typography.labelSmall)
            Divider()
            Text("IPv6 Mode: $ipv6"); Row{ listOf("auto","disable").forEach{ Button(onClick={ipv6=it}, modifier=Modifier.padding(4.dp)){ Text(it) } } }
            Text("If disabled or not configured, IPv6 warning shown. Do not silently leak.", style=MaterialTheme.typography.labelSmall)
            Divider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){ Text("Split Tunnel"); Switch(split,{split=it}) }
            Text("When off: AllowedIPs = 0.0.0.0/0, ::/0 (full tunnel). When on: custom AllowedIPs.", style=MaterialTheme.typography.labelSmall)
        }}
        Button(onClick={ nav.navigate("security")}, modifier=Modifier.fillMaxWidth()){ Text("Security & Privacy Details") }
        OutlinedButton(onClick={ nav.navigate("about")}, modifier=Modifier.fillMaxWidth()){ Text("About & Limitations") }
    }
}
