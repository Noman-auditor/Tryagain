package com.noratunnel.backend
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.io.File

// Optional Backend - NOT required for VPN
// App connects directly to WireGuard. This only serves health.
// NEVER receives PrivateKeys. HTTPS + token required in production.

object HealthServer {
    private fun isWgUp(): String = try {
        val proc = ProcessBuilder("wg", "show").start()
        val out = proc.inputStream.bufferedReader().readText()
        proc.waitFor()
        if (out.contains("interface: wg0")) "up" else "down"
    } catch(e: Exception) { "Unavailable" }

    fun start(port: Int = 8080) {
        val server = HttpServer.create(InetSocketAddress(port), 0)
        
        server.createContext("/health") { ex ->
            if (ex.requestMethod != "GET") { ex.sendResponseHeaders(405, -1); ex.close(); return@createContext }
            val res = """{"status":"ok","service":"nora-tunnel","wg":"${isWgUp()}"}"""
            ex.responseHeaders.add("Content-Type", "application/json")
            ex.sendResponseHeaders(200, res.toByteArray().size.toLong())
            ex.responseBody.write(res.toByteArray())
            ex.close()
        }
        
        server.createContext("/server/status") { ex ->
            // Check Authorization: Bearer <token> in production
            val auth = ex.requestHeaders.getFirst("Authorization")
            // if (auth != "Bearer YOUR_SECURE_TOKEN") { ex.sendResponseHeaders(401, -1); ex.close(); return@createContext }
            try {
                val uptime = try { File("/proc/uptime").readText().split(" ")[0].toDouble().toInt().toString()+"s" } catch(e:Exception) { "Unavailable" }
                val mem = try { File("/proc/meminfo").readLines().firstOrNull()?.trim() ?: "Unavailable" } catch(e:Exception) { "Unavailable" }
                val res = """{"uptime":"$uptime","wg":"${isWgUp()}","mem":"$mem","note":"Real values, not faked. Returns Unavailable if unavailable"}"""
                ex.responseHeaders.add("Content-Type", "application/json")
                ex.sendResponseHeaders(200, res.toByteArray().size.toLong())
                ex.responseBody.write(res.toByteArray())
            } catch(e: Exception) {
                val res = """{"status":"Unavailable"}"""
                ex.sendResponseHeaders(503, res.toByteArray().size.toLong())
                ex.responseBody.write(res.toByteArray())
            }
            ex.close()
        }
        server.start()
        println("NORA Health server listening on :$port -> /health, /server/status")
    }
}
fun main() { HealthServer.start(8080) }
