package com.nora.tunnel

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.rememberNavController
import com.nora.tunnel.core.model.TunnelProfile
import com.nora.tunnel.notification.NotificationHelper
import com.nora.tunnel.tunnel.ConnectionState
import com.nora.tunnel.tunnel.NoraVpnService
import com.nora.tunnel.ui.navigation.NoraNavHost
import com.nora.tunnel.ui.theme.NoraTheme

class MainActivity : ComponentActivity() {
    private var pendingProfile: TunnelProfile? = null

    private val vpnPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            pendingProfile?.let { startVpn(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannel(this)
        setContent { NoraTheme { NoraNavHost(rememberNavController()) } }
    }

    fun requestConnect(profile: TunnelProfile) {
        pendingProfile = profile
        val intent = VpnService.prepare(this)
        if(intent != null) {
            vpnPermission.launch(intent)
        } else {
            startVpn(profile)
        }
    }

    private fun startVpn(profile: TunnelProfile) {
        startService(Intent(this, NoraVpnService::class.java).apply {
            action = NoraVpnService.ACTION_CONNECT
            putExtra("profile", profile)
        })
    }
}
