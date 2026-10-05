package com.nora.tunnel.diagnostics

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.core.content.getSystemService
import com.nora.tunnel.data.datastore.DataStoreManager
import com.nora.tunnel.tunnel.ConnectionState
import com.nora.tunnel.tunnel.NoraVpnService
import kotlinx.coroutines.runBlocking

object DiagnosticsReport {
    fun generateReport(context: Context, state: ConnectionState): String = """
Nora Tunnel Diagnostics
Android: ${Build.VERSION.RELEASE} (API${Build.VERSION.SDK_INT})
Arch: ${Build.SUPPORTED_ABIS.firstOrNull()}
VPN state: ${if(NoraVpnService.instance != null) "ACTIVE" else "INACTIVE"}
Connection state: $state
DNS: ${runBlocking { DataStoreManager(context).getDns() }}
Network: ${context.getSystemService<ConnectivityManager>()?.activeNetworkInfo?.typeName ?: "UNKNOWN"}
""".trimIndent()
}
