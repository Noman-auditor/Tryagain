package com.nora.tunnel.data.secure

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val prefs = EncryptedSharedPreferences.create(
        context, "secure_creds", masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSecret(profileId: String, password: String) = prefs.edit().putString(profileId, password).apply()
    fun getSecret(profileId: String) = prefs.getString(profileId, null)
    
    // Logs redaction
    fun redact(log: String) = log.replace(Regex("(password|privateKey|token)=[^\\s]+"), "$1=******")
}
