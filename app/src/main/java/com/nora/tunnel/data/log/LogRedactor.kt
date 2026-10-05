package com.nora.tunnel.data.log

object LogRedactor {
    private val patterns = listOf(
        Regex("password=\\S+"),
        Regex("privateKey=\\S+"),
        Regex("token=\\S+"),
        Regex("psk\\s*=\\s*\\S+")
    )
    
    fun redact(line: String): String = patterns.fold(line) { acc, r -> 
        acc.replace(r, "${r.pattern.substringBefore("=")}=******") 
    }
}
