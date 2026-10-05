package com.nora.tunnel.tunnel

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.nora.tunnel.core.model.TunnelProfile
import com.nora.tunnel.data.datastore.DataStoreManager
import com.nora.tunnel.data.datastore.Mode
import com.nora.tunnel.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NoraVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            "CONNECT" -> {
                val profile = intent.getParcelableExtra<TunnelProfile>("profile")!!
                scope.launch { establishVpn(profile) }
            }
            "DISCONNECT" -> { teardown(); stopSelf() }
        }
        return START_NOT_STICKY
    }

    private suspend fun establishVpn(profile: TunnelProfile) {
        // 1. Validate
        if(profile.serverAddress.isBlank() || profile.port !in 1..65535) {
            // broadcastState equivalent error handling
            return
        }
        try {
            val builder = Builder()
                .addAddress("10.8.0.2", 32)
                .addRoute("0.0.0.0", 0)
                .addDnsServer("1.1.1.1")
                .setSession(profile.name)
                .setMtu(1500)
            
            // Per-App Routing
            val prefs = DataStoreManager.getAppRoutingMode()
            if(prefs.mode == Mode.SELECTED) prefs.apps.forEach { builder.addAllowedApplication(it) }
            if(prefs.mode == Mode.EXCLUDED) prefs.apps.forEach { builder.addDisallowedApplication(it) }

            vpnInterface = builder.establish() ?: throw IllegalStateException("VpnService not prepared - permission denied")
            
            // Start Foreground Notification - REQUIRED for Android 14+
            startForeground(1, NotificationHelper.createConnectedNotification(this, profile))

            // Delegate to Core Adapter (example placeholder)
            // TunnelManager.getAdapter(profile.core).connect(profile, this)

        } catch(e: Exception) {
            Log.e("NoraVpn", "Failed", e)
            teardown()
        }
    }

    private fun teardown() {
        try { vpnInterface?.close() } catch(_:Exception){}
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
    override fun onRevoke() { teardown(); super.onRevoke() }
    override fun onDestroy() { teardown(); super.onDestroy() }
}
