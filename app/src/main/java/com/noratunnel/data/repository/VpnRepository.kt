package com.noratunnel.data.repository
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.noratunnel.domain.model.VpnState
import com.noratunnel.domain.model.VpnStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class VpnRepository @Inject constructor(@ApplicationContext private val ctx: Context){
    private val _status = MutableStateFlow(VpnStatus())
    val status: StateFlow<VpnStatus> = _status
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs
    fun log(msg: String){
        val line = "${java.time.LocalTime.now().withNano(0)} $msg"
        _logs.value = (_logs.value + line).takeLast(500)
    }
    fun updateState(s: VpnState, name: String? = _status.value.serverName, error: String? = null){
        _status.value = _status.value.copy(state=s, serverName=name, error=error)
        log("${s.name} ${name?:""} ${error?:""}".trim())
    }
    fun updateHealth(latency: Int, stats: Any?){
        _status.value = _status.value.copy(latencyMs=latency, health= if(latency<150) "Healthy" else "Degraded")
    }
    fun buildNotification(ctx: Context, name: String): Notification {
        val ch = "nora_vpn"
        val nm = ctx.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel(ch,"NORA TUNNEL", NotificationManager.IMPORTANCE_LOW))
        return NotificationCompat.Builder(ctx,ch)
            .setContentTitle("NORA TUNNEL - $name")
            .setContentText("Secure. Private. Connected.")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setOngoing(true).build()
    }
}
