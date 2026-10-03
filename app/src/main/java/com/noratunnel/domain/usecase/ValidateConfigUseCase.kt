package com.noratunnel.domain.usecase
import android.util.Base64
import javax.inject.Inject
class ValidateConfigUseCase @Inject constructor(){
    fun isValidBase64Key(k: String): Boolean = try{
        val b = Base64.decode(k.trim(), Base64.DEFAULT); b.size==32
    } catch(e:Exception){ false }
    fun validate(content: String): Result<Unit> {
        if(!content.contains("[Interface]")) return Result.failure(IllegalArgumentException("Missing [Interface]"))
        if(!content.contains("[Peer]")) return Result.failure(IllegalArgumentException("Missing [Peer]"))
        val pk = Regex("""PrivateKey\s*=\s*(\S+)""").find(content)?.groupValues?.get(1) ?: return Result.failure(IllegalArgumentException("Missing PrivateKey"))
        val pub = Regex("""PublicKey\s*=\s*(\S+)""").find(content)?.groupValues?.get(1) ?: return Result.failure(IllegalArgumentException("Missing PublicKey"))
        if(!isValidBase64Key(pk)) return Result.failure(IllegalArgumentException("Invalid PrivateKey"))
        if(!isValidBase64Key(pub)) return Result.failure(IllegalArgumentException("Invalid PublicKey"))
        if(!content.contains("Endpoint")) return Result.failure(IllegalArgumentException("Missing Endpoint"))
        if(!content.contains("Address")) return Result.failure(IllegalArgumentException("Missing Address"))
        return Result.success(Unit)
    }
}
