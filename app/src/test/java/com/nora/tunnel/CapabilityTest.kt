package com.nora.tunnel

import com.nora.tunnel.core.CapabilityRegistry
import com.nora.tunnel.core.model.Core
import com.nora.tunnel.core.model.Protocol
import com.nora.tunnel.core.model.Transport
import com.nora.tunnel.data.log.LogRedactor
import org.junit.Assert.*
import org.junit.Test

class CapabilityTest {
    @Test 
    fun `invalid combo fails`() {
        assertFalse(CapabilityRegistry.isValid(Protocol.WIREGUARD, Core.XRAY, Transport.GRPC))
    }
    
    @Test 
    fun `redaction works`() {
        assertEquals("password=******", LogRedactor.redact("password=123456"))
    }
}
