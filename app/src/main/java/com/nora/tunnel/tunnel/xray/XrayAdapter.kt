package com.nora.tunnel.tunnel.xray

import com.nora.tunnel.core.CapabilityRegistry
import com.nora.tunnel.core.model.TunnelProfile
import com.nora.tunnel.tunnel.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class XrayAdapter : TunnelAdapter {
    override suspend fun validate(p: TunnelProfile): Result<Unit> = 
        if(!CapabilityRegistry.isValid(p.protocol, p.core, p.transport)) 
            Result.failure(Exception("Unsupported combination: ${p.protocol} + ${p.core} + ${p.transport}"))
        else Result.success(Unit)

    override suspend fun prepare(profile: TunnelProfile): Result<Unit> = Result.success(Unit)
    
    override suspend fun connect(profile: TunnelProfile, vpnService: NoraVpnService): Result<ConnectionState> {
        return Result.failure(Exception("Not available in this build - Xray core not bundled. Add libXray.aar to libs/"))
    }
    override suspend fun disconnect() {}
    override fun status(): Flow<ConnectionState> = flowOf(ConnectionState.DISCONNECTED)
    override fun statistics(): Flow<TrafficStats> = flowOf(TrafficStats(0, 0))
    override fun diagnostics() = DiagnosticsResult("Xray", false, "Core not bundled")
}
