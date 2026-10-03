package com.noratunnel.domain.model
enum class VpnState { DISCONNECTED, CONNECTING, CONNECTED, RECONNECTING, DISCONNECTING, ERROR }
data class VpnStatus(
    val state: VpnState = VpnState.DISCONNECTED,
    val serverName: String? = null,
    val latencyMs: Int? = null,
    val handshakeSecondsAgo: Long? = null,
    val health: String = "Unavailable",
    val uploadBytes: Long = 0,
    val downloadBytes: Long = 0,
    val vpnIp: String? = null,
    val error: String? = null,
    val durationSeconds: Long = 0
)
