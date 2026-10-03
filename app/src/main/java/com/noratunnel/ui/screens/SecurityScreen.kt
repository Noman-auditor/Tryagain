ackage com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SecurityScreen(nav: NavController){
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("Security", style=MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
            Text("Secure Storage", style=MaterialTheme.typography.titleMedium)
            Text("• Keys in EncryptedSharedPreferences + Android Keystore (AES256-GCM)\n• Never in Git, logs, analytics, crash reports, plain prefs\n• No hardcoded keys/passwords/tokens", style=MaterialTheme.typography.bodyMedium)
            Divider()
            Text("Privacy", style=MaterialTheme.typography.titleMedium)
            Text("• No ads SDK\n• No hidden analytics\n• No traffic inspection\n• No HTTPS interception\n• No packet logging\n• No hidden proxy", style=MaterialTheme.typography.bodyMedium)
            Divider()
            Text("Kill Switch Limitations", style=MaterialTheme.typography.titleMedium)
            Text("Android 8+ supports Always-on VPN lockdown. Requires user to enable in system. Some OEMs modify behavior. App explains and does not falsely claim blocking.", style=MaterialTheme.typography.bodySmall)
        }}
    }
}
