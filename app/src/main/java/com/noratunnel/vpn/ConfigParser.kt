package com.noratunnel.vpn
import com.noratunnel.domain.model.ServerProfile
import com.noratunnel.security.KeystoreHelper
object ConfigParser {
    fun parse(content: String, name: String, keystore: KeystoreHelper): ServerProfile {
        fun find(p: String) = Regex(p, RegexOption.MULTILINE).find(content)?.groupValues?.get(1)?.trim()
        val privateKey = find("""PrivateKey\s*=\s*(\S+)""") ?: error("Missing PrivateKey")
        val address = find("""Address\s*=\s*([^\n]+)""") ?: error("Missing Address")
        val dns = find("""DNS\s*=\s*([^\n]+)""") ?: "10.8.0.1"
        val pub = find("""PublicKey\s*=\s*(\S+)""") ?: error("Missing PublicKey")
        val endpoint = find("""Endpoint\s*=\s*(\S+)""") ?: error("Missing Endpoint")
        val allowed = find("""AllowedIPs\s*=\s*([^\n]+)""") ?: "0.0.0.0/0, ::/0"
        val keep = find("""PersistentKeepalive\s*=\s*(\d+)""")?.toIntOrNull() ?: 25
        val ipv4 = address.split(",").firstOrNull{ it.contains(".") }?.trim() ?: address.split(",")[0].trim()
        val ipv6 = address.split(",").firstOrNull{ it.contains(":") }?.trim()
        val alias = keystore.encryptAndStore(privateKey)
        return ServerProfile(name=name, endpoint=endpoint, serverPublicKey=pub, encryptedPrivateKeyAlias=alias, clientAddress=ipv4, clientIpv6=ipv6, dns=dns, allowedIps=allowed, persistentKeepalive=keep)
    }
}
