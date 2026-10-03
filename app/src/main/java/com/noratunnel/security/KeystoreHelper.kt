package com.noratunnel.security
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.UUID
class KeystoreHelper(context: Context) {
    private val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val prefs = EncryptedSharedPreferences.create(
        context, "nora_secure_v2", masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    fun encryptAndStore(plainPrivateKey: String): String {
        val alias = UUID.randomUUID().toString()
        prefs.edit().putString(alias, plainPrivateKey).apply()
        return alias
    }
    fun decrypt(alias: String): String = prefs.getString(alias, null) ?: error("Private key alias not found")
    fun delete(alias: String){ prefs.edit().remove(alias).apply() }o

