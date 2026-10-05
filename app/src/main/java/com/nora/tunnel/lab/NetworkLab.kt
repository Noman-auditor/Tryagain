package com.nora.tunnel.lab

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import java.net.InetAddress
import javax.inject.Inject

enum class TestResult { PASS, FAIL, NOT_SUPPORTED, NOT_TESTED }

class NetworkLab @Inject constructor(private val context: Context) {
    suspend fun ping(host: String): TestResult = withContext(Dispatchers.IO) {
        try { 
            val r = Runtime.getRuntime().exec("ping -c 3 $host").waitFor()
            if(r == 0) TestResult.PASS else TestResult.FAIL 
        } catch(e: Exception) { TestResult.FAIL }
    }
    
    suspend fun tcpTest(host: String, port: Int): TestResult = withContext(Dispatchers.IO) {
        try { 
            Socket().use { 
                it.connect(InetSocketAddress(host, port), 3000)
                TestResult.PASS 
            } 
        } catch(_: Exception) { TestResult.FAIL }
    }

    suspend fun dnsTest(host: String): TestResult = withContext(Dispatchers.IO) {
        try { 
            InetAddress.getByName(host)
            TestResult.PASS 
        } catch(_: Exception) { TestResult.FAIL }
    }
}
