package com.nora.tunnel.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.nora.tunnel.core.model.TunnelProfile
import com.nora.tunnel.tunnel.NoraVpnService
import com.nora.tunnel.tunnel.TrafficStats

object NotificationHelper {
    const val CHANNEL_ID = "nora_vpn"
    
    fun createChannel(context: Context) {
        if(Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel(CHANNEL_ID, "Nora Tunnel", NotificationManager.IMPORTANCE_LOW)
            context.getSystemService<NotificationManager>()?.createNotificationChannel(ch)
        }
    }
    
    fun build(context: Context, profile: TunnelProfile, stats: TrafficStats): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setContentTitle("Nora Tunnel • ${profile.name}")
            .setContentText("● CONNECTED • ↓ ${stats.rxBytes} ↑ ${stats.txBytes}")
            .setOngoing(true)
            .addAction(0, "DISCONNECT", PendingIntent.getService(context, 0, Intent(context, NoraVpnService::class.java).setAction(NoraVpnService.ACTION_DISCONNECT), PendingIntent.FLAG_IMMUTABLE))
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }
}
