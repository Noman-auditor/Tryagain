package com.nora.tunnel.domain.usecase

import com.nora.tunnel.core.CapabilityRegistry
import com.nora.tunnel.core.model.TunnelProfile

sealed class ImportResult {
    data class Preview(val profile: TunnelProfile) : ImportResult()
    data class Error(val field: String, val reason: String, val action: String) : ImportResult()
}

class ImportUseCase {
    fun import(input: String): ImportResult {
        val trimmed = input.trim()
        return when {
            trimmed.startsWith("wireguard://") || trimmed.contains("[Interface]") -> parseWireGuard(trimmed)
            trimmed.startsWith("vless://") -> parseVless(trimmed)
            trimmed.startsWith("vmess://") -> parseVmess(trimmed)
            trimmed.startsWith("trojan://") || trimmed.startsWith("ss://") -> parseTrojanOrSS(trimmed)
            trimmed.startsWith("{") -> parseJson(trimmed)
            trimmed.contains("client") && trimmed.contains("remote") -> parseOvpn(trimmed)
            else -> ImportResult.Error(field = "format", reason = "Unknown configuration format", action = "Check URI or JSON and try again")
        }
    }

    private fun parseWireGuard(input: String): ImportResult {
        return ImportResult.Error("wireguard", "WG Parser placeholder", "Provide valid WG config")
    }

    private fun parseVless(uri: String): ImportResult {
        return try {
            ImportResult.Error("vless", "Vless URI parser placeholder", "Verify link format")
        } catch (e: Exception) {
            ImportResult.Error("uri", e.message ?: "Parse failed", "Verify the link is copied completely")
        }
    }

    private fun parseVmess(uri: String): ImportResult = ImportResult.Error("vmess", "VMess parser placeholder", "Check format")
    private fun parseTrojanOrSS(uri: String): ImportResult = ImportResult.Error("protocol", "Parser placeholder", "Check format")
    private fun parseJson(json: String): ImportResult = ImportResult.Error("json", "JSON parser placeholder", "Check format")
    private fun parseOvpn(config: String): ImportResult = ImportResult.Error("ovpn", "OpenVPN parser placeholder", "Check format")
}
