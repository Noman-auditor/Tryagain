package com.noratunnel
import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.noratunnel.domain.model.ServerProfile
import com.noratunnel.ui.navigation.NoraNavGraph
import com.noratunnel.ui.theme.NoraTunnelTheme
import com.noratunnel.vpn.NoraVpnService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var pendingProfile: ServerProfile? = null
    private val vpnPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            pendingProfile?.let { startVpn(it) }
        }
    }
    fun requestVpnPermission(profile: ServerProfile) {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            pendingProfile = profile
            vpnPermissionLauncher.launch(intent)
        } else {
            startVpn(profile)
        }
    }
    private fun startVpn(profile: ServerProfile) {
        val intent = Intent(this, NoraVpnService::class.java).apply {
            putExtra("ACTION", "CONNECT")
            putExtra("PROFILE", profile)
        }
        startForegroundService(intent)
    }
    fun stopVpn() {
        val intent = Intent(this, NoraVpnService::class.java).apply { putExtra("ACTION", "DISCONNECT") }
        startService(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoraTunnelTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NoraNavGraph(activity = this)
                }
            }
        }
    }
}
