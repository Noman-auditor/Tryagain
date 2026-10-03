package com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AboutScreen(nav: NavController){
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("NORA TUNNEL", style=MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Text("Secure. Private. Connected.", style=MaterialTheme.typography.titleSmall)
        Divider()
        Text("A self-hosted VPN can hide your ISP-facing public IP from normal destination services by routing traffic through your VPN server (Phone -> VPS -> Internet).", style=MaterialTheme.typography.bodyMedium)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)){ Column(Modifier.padding(12.dp)){
            Text("However - NOT guaranteed anonymity:", style=MaterialTheme.typography.titleSmall)
            Text("• Websites can recognize known VPN/datacenter IP ranges\n• Websites can use their own security and abuse controls\n• VPN does not guarantee anonymity\n• Account identity can still reveal you\n• Cookies and browser/device signals can identify sessions\n• Some services intentionally restrict VPN traffic\nDo NOT attempt to circumvent those systems.", style=MaterialTheme.typography.bodySmall)
        }}
        Text("Built with official WireGuard Go backend (GPLv2). No custom crypto. Uses real VpnService.", style=MaterialTheme.typography.labelSmall)
        Text("Version 1.0.0 • Target SDK 34 • Min SDK 29", style=MaterialTheme.typography.labelSmall)
    }
}
