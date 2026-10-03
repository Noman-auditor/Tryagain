package com.noratunnel.vpn
import com.wireguard.android.backend.Tunnel
class WireGuardTunnel(private val nameValue: String, private var config: String) : Tunnel {
    override fun getName(): String = nameValue
    override fun onStateChange(newState: Tunnel.State) {}
    fun getConfig(): String = config
    fun setConfig(c: String){ config=c }
}
