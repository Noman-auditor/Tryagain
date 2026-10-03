package com.noratunnel.vpn
import android.net.*
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class NetworkMonitor @Inject constructor(private val cm: ConnectivityManager){
    private var onReconnect: (()->Unit)?=null
    private var scope: CoroutineScope? = null
    private var backoff = 1000L
    private val callback = object: ConnectivityManager.NetworkCallback(){
        override fun onLost(n: Network){ scope?.launch { onReconnect?.invoke() } }
        override fun onAvailable(n: Network){
            scope?.launch { delay(backoff); backoff = minOf(backoff*2, 60000); onReconnect?.invoke() }
        }
    }
    fun register(s: CoroutineScope, cb: ()->Unit){
        scope=s; onReconnect=cb; backoff=1000L
        cm.registerDefaultNetworkCallback(callback)
    }
    fun unregister(){
        try{ cm.unregisterNetworkCallback(callback) }catch(_:Exception){}
        scope=null
    }
    fun resetBackoff(){ backoff=1000L }
}
