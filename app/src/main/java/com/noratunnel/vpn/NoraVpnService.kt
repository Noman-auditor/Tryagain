package com.noratunnel.vpn
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import com.noratunnel.data.repository.VpnRepository
import com.noratunnel.domain.model.ServerProfile
import com.noratunnel.domain.model.VpnState
import com.noratunnel.security.KeystoreHelper
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.net.InetAddress
import javax.inject.Inject

@AndroidEntryPoint
class NoraVpnService : VpnService() {
    @Inject lateinit var repo: VpnRepository
    @Inject lateinit var keystore: KeystoreHelper
    @Inject lateinit var monitor: NetworkMonitor
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var pfd: ParcelFileDescriptor? = null
    private var backend: GoBackend? = null
    private var tunnel: WireGuardTunnel? = null
    private var connectJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        backend = GoBackend(this)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.getStringExtra("ACTION")){
            "DISCONNECT" -> disconnect()
            "CONNECT" -> {
                val profile = intent.getParcelableExtra<ServerProfile>("PROFILE") ?: return START_NOT_STICKY
                connect(profile)
            }
        }
        return START_STICKY
    }
    private fun connect(profile: ServerProfile){
        if(connectJob?.isActive==true) return
        connectJob = scope.launch {
            repo.updateState(VpnState.CONNECTING, profile.name)
            try{
                withTimeout(20000){
                    val privateKey = keystore.decrypt(profile.encryptedPrivateKeyAlias)
                    val builder = Builder().setSession("NORA TUNNEL").setMtu(profile.mtu).setBlocking(true)
                    builder.addAddress(profile.clientAddress.split("/")[0], prefix(profile.clientAddress))
                    profile.clientIpv6?.let{ builder.addAddress(it.split("/")[0], prefix(it)) }
                    profile.dns.split(",").forEach{ if(it.trim().isNotEmpty()) builder.addDnsServer(it.trim().split("/")[0]) }
                    if(profile.allowedIps.contains("0.0.0.0/0")) builder.addRoute("0.0.0.0",0)
                    else profile.allowedIps.split(",").filter{it.contains(".")}.forEach{ val (ip,pr)=it.trim().split("/"); builder.addRoute(ip, pr.toInt()) }
                    if(profile.allowedIps.contains("::/0") && profile.clientIpv6!=null) builder.addRoute("::",0)
                    pfd?.close()
                    pfd = builder.establish() ?: error("VpnService not prepared - permission denied")
                    val wgConf = buildString{
                        appendLine("[Interface]"); appendLine("PrivateKey = $privateKey")
                        appendLine("Address = ${profile.clientAddress}${profile.clientIpv6?.let{", $it"}?: ""}")
                        appendLine("DNS = ${profile.dns}"); appendLine("[Peer]")
                        appendLine("PublicKey = ${profile.serverPublicKey}"); appendLine("Endpoint = ${profile.endpoint}")
                        appendLine("AllowedIPs = ${profile.allowedIps}"); appendLine("PersistentKeepalive = ${profile.persistentKeepalive}")
                    }
                    tunnel = WireGuardTunnel(profile.name, wgConf)
                    backend!!.setState(tunnel, Tunnel.State.UP, pfd!!.fd)
                    var tries=0
                    while(tries<30){ delay(500); if(tries>3) break; tries++ }
                    repo.updateState(VpnState.CONNECTED, profile.name)
                    monitor.register(scope){ handleNetworkChange(profile) }
                    startForeground(1, repo.buildNotification(this@NoraVpnService, profile.name))
                    monitor.resetBackoff()
                    launchHealthLoop(profile)
                }
            } catch(e: Exception){
                repo.updateState(VpnState.ERROR, error=e.message)
                disconnect()
            }
        }
    }
    private fun handleNetworkChange(profile: ServerProfile){
        scope.launch{ repo.updateState(VpnState.RECONNECTING, profile.name); delay(1000); repo.updateState(VpnState.CONNECTED, profile.name) }
    }
    private fun launchHealthLoop(profile: ServerProfile){
        scope.launch{
            while(isActive){
                delay(5000)
                try{
                    val stats = backend?.getStatistics(tunnel)
                    val start = System.currentTimeMillis()
                    val reachable = InetAddress.getByName(profile.endpoint.split(":")[0]).isReachable(2000)
                    val latency = (System.currentTimeMillis()-start).toInt()
                    repo.updateHealth(latency, stats)
                } catch(_: Exception){}
            }
        }
    }
    private fun disconnect(){
        scope.launch{
            repo.updateState(VpnState.DISCONNECTING)
            monitor.unregister()
            try{ tunnel?.let{ backend?.setState(it, Tunnel.State.DOWN, -1) } }catch(_:Exception){}
            pfd?.close(); pfd=null
            repo.updateState(VpnState.DISCONNECTED)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }
    private fun prefix(cidr: String): Int = if(cidr.contains("/")) cidr.split("/")[1].toInt() else 32
    override fun onRevoke(){ disconnect(); super.onRevoke() }
    override fun onDestroy(){ disconnect(); super.onDestroy() }
}
